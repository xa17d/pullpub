package at.xa1.pullpub.server.repository

class MockRepository : Repository {
    var pullResult: PullResult = PullResult.AlreadyUpToDate
    var pullThrows: Boolean = false

    var activeCommit: Commit = Commit(
        "ID",
        "Mocked commit message",
        "Mock Author"
    )

    override suspend fun pull(): PullResult {
        if (pullThrows) {
            throw RepositoryException("pullThrows is set to true")
        }
        return pullResult
    }

    override suspend fun getActiveCommit(): Commit = activeCommit
}
