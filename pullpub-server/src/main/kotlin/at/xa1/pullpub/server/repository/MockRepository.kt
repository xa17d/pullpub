package at.xa1.pullpub.server.repository

class MockRepository : Repository {
    var pullResult: PullResult = PullResult.AlreadyUpToDate
    var pullThrows: Boolean = false

    var activeCommit: Commit = Commit(
        "ID",
        "2019-09-01 11:22:33",
        "Mocked commit message",
        "Mock Author"
    )
    var activeCommitThrows = false

    override suspend fun pull(): PullResult {
        if (pullThrows) {
            throw RepositoryException("pullThrows is set to true")
        }
        return pullResult
    }

    override suspend fun getActiveCommit(): Commit {
        if (activeCommitThrows) {
            throw RepositoryException("pullThrows is set to true")
        }
        return activeCommit
    }
}
