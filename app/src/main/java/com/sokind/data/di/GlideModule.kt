package com.sokind.data.di

import android.content.Context
import android.graphics.drawable.PictureDrawable
import androidx.annotation.NonNull
import com.bumptech.glide.Glide
import com.bumptech.glide.Registry
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.load.Options
import com.bumptech.glide.load.ResourceDecoder
import com.bumptech.glide.load.engine.Resource
import com.bumptech.glide.module.AppGlideModule
import com.caverock.androidsvg.SVG
import java.io.InputStream
import com.caverock.androidsvg.SVGParseException

import com.bumptech.glide.load.resource.SimpleResource
import com.bumptech.glide.load.resource.transcode.ResourceTranscoder

import com.bumptech.glide.request.target.Target.SIZE_ORIGINAL
import java.io.IOException
import android.graphics.Picture

@GlideModule
class GlideModule: AppGlideModule() {
    override fun registerComponents(context: Context, glide: Glide, registry: Registry) {
        registry
            .register(SVG::class.java, PictureDrawable::class.java, SvgDrawableTranscoder())
            .append(InputStream::class.java, SVG::class.java, SvgDecoder())
    }

    inner class SvgDrawableTranscoder: ResourceTranscoder<SVG, PictureDrawable> {
        override fun transcode(
            toTranscode: Resource<SVG>,
            options: Options
        ): Resource<PictureDrawable>? {
            val svg = toTranscode.get()
            val picture = svg.renderToPicture()
            val drawable = PictureDrawable(picture)
            return SimpleResource(drawable)
        }
    }

    inner class SvgDecoder: ResourceDecoder<InputStream, SVG> {
        override fun handles(source: InputStream, options: Options): Boolean {
            // TODO: Can we tell?
            return true
        }

        override fun decode(
            source: InputStream,
            width: Int,
            height: Int,
            options: Options
        ): Resource<SVG>? {
            return try {
                val svg = SVG.getFromInputStream(source)
                if (width != SIZE_ORIGINAL) {
                    svg.documentWidth = width.toFloat()
                }
                if (height != SIZE_ORIGINAL) {
                    svg.documentHeight = height.toFloat()
                }
                SimpleResource(svg)
            } catch (ex: SVGParseException) {
                throw IOException("Cannot load SVG from stream", ex)
            }
        }
    }

    override fun isManifestParsingEnabled(): Boolean {
        return false
    }
}