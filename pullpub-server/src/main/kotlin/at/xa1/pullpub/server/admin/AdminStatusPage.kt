package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.logging.EventLogRepository
import kotlinx.html.*

fun HTML.adminStatusPage(logRepository: EventLogRepository) {
    body {
        style("text/css") {

        }

        h1 { +"pullpub" }

        h1 { +"log" }

        table {
            logRepository.getLatest().forEach {
                tr {
                    td { +it.tag }
                    td { +it.message }
                }
            }
        }
    }
}
