/*
 *  Copyright (c) 2022-2022 ForteScarlet <ForteScarlet@163.com>
 *
 *  本文件是 simbot-component-kaiheila 的一部分。
 *
 *  simbot-component-kaiheila 是自由软件：你可以再分发之和/或依照由自由软件基金会发布的 GNU 通用公共许可证修改之，无论是版本 3 许可证，还是（按你的决定）任何以后版都可以。
 *
 *  发布 simbot-component-kaiheila 是希望它能有用，但是并无保障;甚至连可销售和符合某个特定的目的都不保证。请参看 GNU 通用公共许可证，了解详情。
 *
 *  你应该随程序获得一份 GNU 通用公共许可证的复本。如果没有，请看:
 *  https://www.gnu.org/licenses
 *  https://www.gnu.org/licenses/gpl-3.0-standalone.html
 *  https://www.gnu.org/licenses/lgpl-3.0-standalone.html
 *
 *
 */

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
            val NOTATION = "${P.Simbot.GROUP}.boot:simboot-api:${P.Simbot.VERSION}"
        }
        object BootCore {
            val NOTATION = "${P.Simbot.GROUP}.boot:simboot-core:${P.Simbot.VERSION}"
        }
        object BootCoreSpringBootStarter {
            val NOTATION = "${P.Simbot.GROUP}.boot:simboot-core-spring-boot-starter:${P.Simbot.VERSION}"
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
            const val VERSION = "1.3.3"

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
        const val VERSION = "2.0.0"

        object Serialization {
            object KotlinxJson {
                const val NOTATION = "$GROUP:ktor-serialization-kotlinx-json:$VERSION"
            }
        }

        object Server {
            object ContentNegotiation {
                const val NOTATION = "$GROUP:ktor-server-content-negotiation:$VERSION"
            }
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
            object ContentNegotiation {
                const val NOTATION = "$GROUP:ktor-client-content-negotiation:$VERSION"
            }
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
