package com.app.vivi.extension

val String.Companion.EMPTY: String
    get() = ""

fun String?.isNotEmpty(action: (it: String) -> Unit) {
    if (this == null) return
    if (this.isNotEmpty()) {
        action(this)
    }
}

fun String?.cutOnText(): String {

    return """
    <html>
        <body>
            <p>
                <span><s>$this</s></span>
            </p>
        </body>
    </html>
""".trimIndent()
}
