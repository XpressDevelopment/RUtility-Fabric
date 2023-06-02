package network.roanoke.rutility

interface RModule {

    /**
     * Returns the name of the module for easier search.
     * @return Module Name
     */
    val name: String

    /**
     * Returns true if the module is enabled. False otherwise.
     * @return true if module is enabled
     */
    fun isEnabled(): Boolean

    /**
     * Sets module enabled status
     */
    fun enable(enabled: Boolean)
}