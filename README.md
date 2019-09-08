# pullpub
Simple webserver that serves static content from a git repository

# Build and Run
## Build and run locally
```bash
./gradlew pullpub-server:jar

cd pullpub-server/build/libs/
java -jar pullpub-server-0.0.1-TEST.jar [PARAMETERS]
```

For `[PARAMETERS]` add the parameters as described when `jar` is run without parameters.

## Build and run Docker Container
```bash
./gradlew pullpub-docker:createDockerImage

cd pullpub-docker/test/
./run_and_connect.sh
```

`run_and_connect.sh` will create a new Container, mount `pullpub-docker/test/config` into `/app/config`.
Port `80` from Docker is mapped to `8080` on the host.

The admin panel can be accessed via `http://localhost:8080/admin/`.

## Run unit tests
```bash#
./gradlew test
```

# Configuration
Docker image name is `xa17d/pullpub`.
It exposes port `80`.
Can be configured by attaching a volume at `/app/config`.
The config folder can contain following files:

* **REQUIRED:** `git-repository`: contains the path to the git repository containing the website. E.g.:
    ```
    https://github.com/xa17d/pullpub-website-example.git
    ```
* **REQUIRED:** `admin-path`: Path to the admin interface. Use a long and random string, because it basically serves as password. E.g.:
    ```
    91c43c04-07d5-4c78-a0c9-3965f048f417
    ```
* `git-branch`: contains the name of the branch to checkout. E.g.:
    ```
    master
    ```
* `git-credentials`: contains git credentials. Each credential is stored on its own line as a URL like:
    ```
    https://user:pass@example.com
    ```
* `gitconfig`: Will be copied to `~/.gitconfig` in the Container.
* `id_rsa`: Private key used for SSH. Will be copied to `~/.ssh/id_rsa` in the Container.
* `id_rsa.pub`: Public key used for SSH. "Will be copied to `~/.ssh/id_rsa.pub` in the Container.