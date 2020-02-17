package mehrpars.mobile.baseutil.common

/**
 * Created by Ali Arasteh on 7/26/2017 - 6:03 PM.
 * Project Name: Ketabkhan.
 */

abstract class Command {
    fun onExecute() {

    }

    fun onExecute(data: Any) {

    }

    fun onCancel() {}
}