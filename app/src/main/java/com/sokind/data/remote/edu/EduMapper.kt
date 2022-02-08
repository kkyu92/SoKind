package com.sokind.data.remote.edu

object EduMapper {
    fun mappingRemoteDataToEdu(
        response: EduUpdateResponse
    ): Edu {
        return Edu(
            response.eduType,
            response.eduKey,
            response.enterpriseKey,
            position = 1,
            response.eduTitle,
            response.subTitle,
            response.eduContents,
            response.eduMent,
            recordFile = "",
            response.eduThumbnail,
            runningTime = 3,
            status = 1
        )
    }
    fun mappingNextEduToEdu(
        nextEdu: NextEdu
    ): Edu {
        return Edu(
            type = nextEdu.type,
            key = nextEdu.key,
            enterpriseKey = nextEdu.enterpriseKey,
            position = nextEdu.position,
            title = nextEdu.title,
            subTitle = nextEdu.subTitle,
            contents = nextEdu.contents,
            ment = nextEdu.ment,
            recordFile = nextEdu.recordFile,
            thumbnail = nextEdu.thumbnail,
            runningTime = nextEdu.runningTime,
            status = nextEdu.status
        )
    }
}