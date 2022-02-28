@Suppress("MayBeConstant", "MemberVisibilityCanBePrivate", "unused")
object V {
    object Simbot {
        object Api {
            val NOTATION = "${P.Simbot.GROUP}:simbot-api:${P.Simbot.VERSION}"
        }
        object Core {
            val NOTATION = "${P.Simbot.GROUP}:simbot-core:${P.Simbot.VERSION}"
        }
        object BootApi {
            val NOTATION = "${P.Simbot.GROUP}:simboot-api:${P.Simbot.VERSION}"
        }
        object BootCore {
            val NOTATION = "${P.Simbot.GROUP}:simboot-core:${P.Simbot.VERSION}"
        }
    }


    object Jetbrains {
        const val GROUP = "org.jetbrains"
        object Annotations {
            const val VERSION = "23.0.0"
            const val NOTATION = "$GROUP:annotations:$VERSION"
        }
    }

    object Kotlin {
        const val GROUP = "org.jetbrains.kotlin"

    }


    object Kotlinx {
        const val GROUP = "org.jetbrains.kotlinx"
        object Serialization {
            const val VERSION = "1.3.1"

            object Core {
                const val NOTATION = "$GROUP:kotlinx-serialization-core:$VERSION"
            }

            object Json {
                const val NOTATION = "$GROUP:kotlinx-serialization-json:$VERSION"
            }

            object Hocon {
                const val NOTATION = "$GROUP:kotlinx-serialization-hocon:$VERSION"
            }

            object Protobuf {
                const val NOTATION = "$GROUP:kotlinx-serialization-protobuf:$VERSION"
            }

            object Cbor {
                const val NOTATION = "$GROUP:kotlinx-serialization-cbor:$VERSION"
            }

            object Properties {
                const val NOTATION = "$GROUP:kotlinx-serialization-properties:$VERSION"
            }

            object Yaml {
                const val NOTATION = "com.charleskorn.kaml:kaml:0.37.0"
            }

        }

    }


    object Ktor {
        const val GROUP = "io.ktor"
        const val VERSION = "1.6.4"

        object Server {
            object Core {
                const val NOTATION = "$GROUP:ktor-server-core:$VERSION"
            }
            object Netty  {
                const val NOTATION = "$GROUP:ktor-server-netty:$VERSION"
            }
            object Jetty  {
                const val NOTATION = "$GROUP:ktor-server-jetty:$VERSION"
            }
            object Tomcat {
                const val NOTATION = "$GROUP:ktor-server-tomcat:$VERSION"
            }
            object CI {
                const val NOTATION = "$GROUP:ktor-server-cio:$VERSION"
            }
        }

        object Client {
            object Serialization {
                const val NOTATION = "$GROUP:ktor-client-serialization:$VERSION"
            }
            object Auth {
                const val NOTATION = "$GROUP:ktor-client-auth:$VERSION"
            }
            object Websockets {
                const val NOTATION = "$GROUP:ktor-client-websockets:$VERSION"
            }
            object Jvm {
                object Core {
                    const val NOTATION = "$GROUP:ktor-client-core:$VERSION"
                }
                object Apache {
                    const val NOTATION = "$GROUP:ktor-client-apache:$VERSION"
                }
                object Java {
                    const val NOTATION = "$GROUP:ktor-client-java:$VERSION"
                }
                object Jetty {
                    const val NOTATION = "$GROUP:ktor-client-jetty:$VERSION"
                }
                object CIO {
                    const val NOTATION = "$GROUP:ktor-client-cio:$VERSION"
                }
                object OkHttp {
                    const val NOTATION = "$GROUP:ktor-client-okhttp:$VERSION"
                }
            }
        }

    }




}