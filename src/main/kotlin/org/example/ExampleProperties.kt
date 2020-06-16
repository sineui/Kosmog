package org.example

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

/*
    https://spring.io/guides/tutorials/spring-boot-kotlin/#_configuration_properties

    Since Kapt is not yet integrated in IDEA(https://youtrack.jetbrains.com/issue/KT-15040),
    you need to run manually the command ./gradlew kaptKotlin to generate the metadata.

    Note that some features (such as detecting the default value or deprecated items) are not working
    due to limitations in the model kapt provides.
 */
@ConstructorBinding
@ConfigurationProperties("org.example")
data class ExampleProperties(
    val jwt: Jwt
) {
    data class Jwt(
        val prefix: String = "bearer ",
        val secret: String,
        val duration: Long = 259200, // 3 days
        val refreshToken: RefreshToken,
        val passwordResetToken: PasswordResetToken
    ) {
        data class RefreshToken(
            val secret: String,
            val duration: Long = 604800 // 7 days
        )

        data class PasswordResetToken(
            val secret: String,
            val duration: Long = 300 // 5 minutes
        )
    }
}
