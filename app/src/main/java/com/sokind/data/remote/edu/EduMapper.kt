package com.sokind.data.remote.edu

import com.sokind.data.remote.report.ReportItem

object EduMapper {
    fun mappingNextEduToEdu(
        nextEdu: NextEdu
    ): Edu {
        return BaseEdu(
            type = nextEdu.type,
            key = nextEdu.key,
            title = nextEdu.title,
            status = nextEdu.status
        )
    }
    fun mappingReportToEdu(
        report: ReportItem
    ): Edu{
        return BaseEdu(
            type = report.type,
            key = report.key,
            title = report.title,
            status = report.status
        )
    }
}