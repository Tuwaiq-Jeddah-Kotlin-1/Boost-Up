import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.mahila.motivationalQuotesApp.util.NotificationHelper
import com.mahila.motivationalQuotesApp.views.homeScreen.MainActivity


class NotificationWorker(
    private val context: Context,
    workParams: WorkerParameters
) : Worker(context, workParams) {
    override fun doWork(): Result {
        NotificationHelper(context).createNotification(
            inputData.getString("title").toString(),
            inputData.getString("msg").toString()
        )

        return Result.success()
    }


}
