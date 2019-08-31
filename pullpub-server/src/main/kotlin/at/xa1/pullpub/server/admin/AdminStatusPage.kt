package at.xa1.pullpub.server.admin

import at.xa1.pullpub.server.logging.EventLogRepository
import at.xa1.pullpub.server.repository.Commit
import kotlinx.html.*

fun HTML.adminStatusPage(logRepository: EventLogRepository, activeCommit: Commit) {
    head {
        style("text/css") {
            unsafe {
                raw(
                    """
                    body {
                      font-family: Arial;
                    }
                        
                    table {
                      border-collapse: collapse;
                    }
                    
                    .round {
                      border-radius: 5px;
                      border: 1px solid #aaaaaa;
                      overflow: hidden;
                      display: inline-block;
                    }
                    
                    th, td {
                      text-align: left;
                      padding: 8px;
                    }
                    
                    th {
                      background-color: #999999;
                      color: #ffffff;
                    }
                    
                    tr:nth-child(even) th {background-color: #aaaaaa;}
                    tr:nth-child(even) td {background-color: #f2f2f2;}
                    """.trimIndent()
                )
            }
        }
        script("text/javascript") {
            unsafe {
                raw(
                    """
                    function pull() {
                        var xhttp = new XMLHttpRequest();
                        xhttp.onreadystatechange = function() {
                            if (this.readyState == 4) {
                                if (this.status == 200) {
                                    // success    
                                }
                                location.reload();
                            }
                        };
                        xhttp.open("POST", "pull", true);
                        xhttp.send();
                    }
                    """.trimIndent()
                )
            }
        }
    }
    body {


        h1 { +"pullpub" }

        button {
            onClick = "pull()"
            +"pull!"
        }

        h2 { +"Active Commit" }
        div(classes = "round") {
            table {
                tr {
                    th { +"ID" }
                    td { +activeCommit.id }
                }
                tr {
                    th { +"Timestamp" }
                    td { +activeCommit.timestamp }
                }
                tr {
                    th { +"Message" }
                    td { +activeCommit.message }
                }
                tr {
                    th { +"Author" }
                    td { +activeCommit.author }
                }
            }
        }

        h2 { +"Log" }

        div(classes = "round") {
            table {
                logRepository.getLatest().forEach {
                    tr {
                        th { +it.tag }
                        td { +it.message }
                    }
                }
            }
        }
    }
}
