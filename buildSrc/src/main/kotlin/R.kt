import love.forte.gradle.common.core.repository.Repositories
import love.forte.gradle.common.core.repository.SimpleCredentials
import org.slf4j.LoggerFactory

val logger = LoggerFactory.getLogger("Sonatype Userinfo")

private val sonatypeUserInfo by lazy {
    val userInfo = love.forte.gradle.common.publication.sonatypeUserInfoOrNull
    
    if (userInfo == null) {
        logger.warn("sonatype.username or sonatype.password is null, cannot config nexus publishing.")
    }
    
    userInfo
}

val sonatypeUsername: String? get() = sonatypeUserInfo?.username
val sonatypePassword: String? get() = sonatypeUserInfo?.password

val ReleaseRepository by lazy {
    Repositories.Central.Default.copy(SimpleCredentials(sonatypeUsername, sonatypePassword))
}
val SnapshotRepository by lazy {
    Repositories.Snapshot.Default.copy(SimpleCredentials(sonatypeUsername, sonatypePassword))
}