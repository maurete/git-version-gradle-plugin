package ch.avelon.git

/**
 * @author Flurin Juvalta <flurin.juvalta@avelon.ch>
 */
class Git {

    boolean isInitialized() {
        return new File('.git').isDirectory()
    }

    boolean hasTags() {
        return !getCurrentTag().isEmpty()
    }


    boolean isOnMasterBranch() {
        return isBranch("master")
    }

    boolean isOnReleaseBranch() {
        return isBranch("release/")
    }

    String getCurrentTag() {
        return execute("git describe --tags")
    }

    String getAllTags() {
        return execute("git tag")
    }

    /**
     * Checks if we are currently on the branch given by the name.
     * Since Jenkins works in detached HEAD mode, we have to check the branch name like this.
     *
     * @param name The name of the branch (or part of it).
     * @return true if the current working directory reflects the state of a branch containing the given name.
     */
    boolean isBranch(String name) {
        return execute("git show -s --pretty=%d HEAD").contains(name)
    }

    String getSHA() {
        return execute("git rev-parse --short HEAD")
    }

    boolean releaseExists() {
        "git remote prune origin".execute()
        return !execute("git branch -a --list release*").isEmpty()
    }

    void createAndPushTag(String version) {
        "git tag -a $version -m $version".execute()
        "git push --tags".execute()
    }

    String execute(String command) {
        command.execute().text.trim()
    }
}
