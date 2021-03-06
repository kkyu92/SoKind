package com.sokind.ui.my.info

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import com.jakewharton.rxbinding4.view.clicks
import com.sokind.R
import com.sokind.data.di.GlideApp
import com.sokind.data.remote.member.MemberInfo
import com.sokind.databinding.ActivityInfoBinding
import com.sokind.ui.MainActivity
import com.sokind.ui.base.BaseActivity
import com.sokind.ui.my.info.change.ChangeActivity
import com.sokind.ui.my.info.secession.SecessionActivity
import com.sokind.util.Constants
import com.sokind.util.dialog.BottomSheetDialog
import dagger.hilt.android.AndroidEntryPoint
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import timber.log.Timber
import java.io.File
import java.io.FileOutputStream
import java.io.OutputStream
import java.util.*
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class InfoActivity : BaseActivity<ActivityInfoBinding>(R.layout.activity_info) {
    private val viewModel by viewModels<InfoViewModel>()

    private val startForResultCamera =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    if (it.data != null) {
                        val bitmap = it.data!!.extras?.get("data") as Bitmap
                        GlideApp.with(this).load(bitmap).centerCrop().into(binding.ivProfile)

                        val file = bitmapToMultipart(bitmap)
                        viewModel.profileUpdate(file)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

    private val startForResultGallery =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                try {
                    if (it.data != null) {
                        val selectedImageUri = it.data!!.data
                        val bitmap = BitmapFactory.decodeStream(
                            this.contentResolver.openInputStream(selectedImageUri!!)
                        )
                        GlideApp.with(this).load(bitmap).centerCrop().into(binding.ivProfile)

                        val file = bitmapToMultipart(bitmap)
                        viewModel.profileUpdate(file)
                    }
                } catch (e: Exception) {
                    Timber.e(e)
                }
            }
        }

    private val startForResultChange =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == Activity.RESULT_OK) {
                if (it.data != null) {
                    binding.tvEmail.text = it.data?.getStringExtra("newEmail")
                }
            }
        }

    override fun init() {
        val user = intent.getSerializableExtra("info") as MemberInfo
        setBinding(user)
        setViewModel()
        setData(user)
    }

    private fun setViewModel() {
        viewModel.apply {
            profile.observe(this@InfoActivity, {
                if (it) {
                    showToast("???????????? ??????????????????.")
                } else {
                    showToast("????????? ????????? ??????????????????.")
                }
            })
            extras.observe(this@InfoActivity, {
                if (it) {
                    Timber.e("extras change SUCCESS")
                } else {
                    Timber.e("extras change FAIL")
                }
            })
            isLogout.observe(this@InfoActivity, {
                if (it) {
                    val intent = Intent(this@InfoActivity, MainActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivity(intent)
                }
            })
        }
    }

    private fun setData(user: MemberInfo) {
        binding.apply {
            GlideApp.with(this@InfoActivity)
                .load(user.profileImg)
                .error(R.drawable.icon_profile_default)
                .into(ivProfile)
            tvUser.text = getString(R.string.name_form, user.memberName, user.positionName)
            tvStore.text = getString(R.string.enterprise_form, user.enterpriseName, user.storeName)
            tvEnterprise.text = user.enterpriseName
            tvId.text = user.memberId
            tvEmail.text = user.memberEmail
            tvName.text = user.memberName
            tvGender.text = if (user.memberGender == 0) "??????" else "??????"
            switchEvent.isChecked = user.eventYN == 1
            switchEmail.isChecked = user.emailYN == 1
            switchApp.isChecked = user.pushYN == 1
        }
    }

    override fun onBackPressed() {
        viewModel.extraUpdate(
            if (binding.switchEvent.isChecked) 1 else 0,
            if (binding.switchEmail.isChecked) 1 else 0,
            if (binding.switchApp.isChecked) 1 else 0
        )
        setResult(RESULT_OK)
        super.onBackPressed()
    }

    private fun setBinding(user: MemberInfo) {
        binding.apply {
            btBack
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    onBackPressed()
                }, { it.printStackTrace() })

            ivEdit
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog =
                        BottomSheetDialog.newInstance(
                            Constants.PROFILE_DIALOG,
                            getString(R.string.dialog_profile, user.memberName),
                            "",
                            itemClick = { okClick ->
                                if (!okClick) {
                                    GlideApp.with(this@InfoActivity)
                                        .load(R.color.transparent).into(ivProfile)
                                }
                            }
                        ) as BottomSheetDialog
                    dialog.show(supportFragmentManager, dialog.tag)

                    dialog.setOnProfileClickListener(object :
                        BottomSheetDialog.OnProfileClickListener {
                        override fun onCameraClick() {
                            startForResultCamera.launch(
                                Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                            )
                        }

                        override fun onGalleryClick() {
                            startForResultGallery.launch(
                                Intent(
                                    Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI
                                )
                            )
                        }
                    })
                }, { it.printStackTrace() })

            tvEmailChange
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(this@InfoActivity, ChangeActivity::class.java)
                    intent.putExtra("title", "????????? ??????")
                    startForResultChange.launch(intent)
                }, { it.printStackTrace() })

            tvPwChange
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(this@InfoActivity, ChangeActivity::class.java)
                    intent.putExtra("title", "???????????? ??????")
                    startForResultChange.launch(intent)
                }, { it.printStackTrace() })

            tvLogout
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val dialog = BottomSheetDialog.newInstance(
                        Constants.SIMPLE_DIALOG,
                        getString(R.string.dialog_logout),
                        "",
                        itemClick = { okClick ->
                            if (okClick) {
                                // logout
                                viewModel.logout()
                            }
                        }
                    ) as BottomSheetDialog
                    dialog.show(supportFragmentManager, dialog.tag)
                }, { it.printStackTrace() })
            tvTerms
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://marazoa.com/sokind/terms"))
                    startActivity(intent)
                }, { it.printStackTrace() })
            tvPrivacy
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://marazoa.com/sokind/policy"))
                    startActivity(intent)
                }, { it.printStackTrace() })
            tvSecession
                .clicks()
                .throttleFirst(Constants.THROTTLE, TimeUnit.MILLISECONDS)
                .subscribe({
                    val intent = Intent(this@InfoActivity, SecessionActivity::class.java)
                    startForResultChange.launch(intent)
                }, { it.printStackTrace() })
        }
    }

    private fun bitmapToMultipart(bitmap: Bitmap): MultipartBody.Part {
        val wrapper = ContextWrapper(this)
        var file = wrapper.getDir("Images", Context.MODE_PRIVATE)
        file = File(file, "${UUID.randomUUID()}.jpg")
        val stream: OutputStream = FileOutputStream(file)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 25, stream)
        stream.flush()
        stream.close()

        val requestBody = file.asRequestBody("image/*".toMediaTypeOrNull())
        return MultipartBody.Part.createFormData("profileImg", file.name, requestBody)
    }
}