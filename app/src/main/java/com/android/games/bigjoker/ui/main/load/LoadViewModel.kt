package com.android.games.bigjoker.ui.main.load

import android.app.Application
import androidx.core.net.toUri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.games.bigjoker.utils.Params
import com.appsflyer.AppsFlyerConversionListener
import com.appsflyer.AppsFlyerLib
import com.facebook.applinks.AppLinkData
import com.google.android.gms.ads.identifier.AdvertisingIdClient
import com.onesignal.OneSignal
import kotlinx.coroutines.*
import java.util.*
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LoadViewModel(private val app: Application) : AndroidViewModel(app) {

    val resultLink = MutableLiveData<String>()

    init {
        viewModelScope.launch {
            val defAF = async(start = CoroutineStart.LAZY) { getDataAppsFlyer(Params.APPS_DEV_KEY) }
            val defFB = async { getFbLink() }.await()
            val defGadid = async { getGadId() }.await()

            OneSignal.setExternalUserId(defGadid)
            withContext(Dispatchers.IO) {
                if (defFB != null) {
                    resultLink.postValue(createResultLink(defFB, defGadid))
                    sendOneSignal(defFB)
                } else {
                    val def = defAF.await()
                    resultLink.postValue(createResultLink(defFB, defGadid))
                    sendOneSignal(def)
                }
            }
        }
    }

    private fun createResultLink(data: MutableMap<String, Any>?, gadid: String?): String {

        return Params.BASE_LINK.toUri().buildUpon().apply {
            appendQueryParameter(Params.SECURE_GET_PARAMETR, Params.SECURE_KEY)
            appendQueryParameter(Params.DEV_TMZ_KEY, TimeZone.getDefault().id)
            appendQueryParameter(Params.GADID_KEY, gadid)
            appendQueryParameter(Params.DEEPLINK_KEY, "null")
            appendQueryParameter(Params.SOURCE_KEY, data?.get("media_source").toString())
            appendQueryParameter(
                Params.AF_ID_KEY,
                AppsFlyerLib.getInstance().getAppsFlyerUID(getApplication())
                )
            appendQueryParameter(Params.ADSET_ID_KEY, data?.get("adset_id").toString())
            appendQueryParameter(Params.CAMPAIGN_ID_KEY, data?.get("campaign_id").toString())
            appendQueryParameter(Params.APP_CAMPAIGN_KEY, data?.get("campaign").toString())
            appendQueryParameter(Params.ADSET_KEY, data?.get("adset").toString())
            appendQueryParameter(Params.ADGROUP_KEY, data?.get("adgroup").toString())
            appendQueryParameter(Params.ORIG_COST_KEY, data?.get("orig_cost").toString())
            appendQueryParameter(Params.AF_SITEID_KEY, data?.get("af_siteid").toString())
        }.toString()
    }

    private fun createResultLink(deep: Any?, gadid: String?): String {

        return Params.BASE_LINK.toUri().buildUpon().apply {
            appendQueryParameter(Params.SECURE_GET_PARAMETR, Params.SECURE_KEY)
            appendQueryParameter(Params.DEV_TMZ_KEY, TimeZone.getDefault().id)
            appendQueryParameter(Params.GADID_KEY, gadid)
            appendQueryParameter(Params.DEEPLINK_KEY, deep.toString())
            appendQueryParameter(Params.SOURCE_KEY, deep.toString())
            appendQueryParameter(Params.AF_ID_KEY, "null")
            appendQueryParameter(Params.ADSET_ID_KEY, "null")
            appendQueryParameter(Params.CAMPAIGN_ID_KEY, "null")
            appendQueryParameter(Params.APP_CAMPAIGN_KEY, "null")
            appendQueryParameter(Params.ADSET_KEY, "null")
            appendQueryParameter(Params.ADGROUP_KEY, "null")
            appendQueryParameter(Params.ORIG_COST_KEY, "null")
            appendQueryParameter(Params.AF_SITEID_KEY, "null")
        }.toString()
    }

    private suspend fun getGadId(): String {
        val id = withContext (Dispatchers.IO) {
            AdvertisingIdClient.getAdvertisingIdInfo(getApplication()).id.toString()
        }
        return id
    }

    private suspend fun getDataAppsFlyer(devKey: String): MutableMap<String, Any>? {

        return suspendCoroutine { continuation ->
            val conversionDataListener = object : AppsFlyerConversionListener {
                override fun onConversionDataSuccess(data: MutableMap<String, Any>?) {
                    continuation.resume(data)
                }
                override fun onConversionDataFail(error: String?) {
                    continuation.resume(null)
                }
                override fun onAppOpenAttribution(data: MutableMap<String, String>?) {}
                override fun onAttributionFailure(error: String?) {}
            }
            AppsFlyerLib.getInstance().init(devKey, conversionDataListener, app.applicationContext)
            AppsFlyerLib.getInstance().start(app.applicationContext)
        }
    }

    private suspend fun getFbLink(): Any? {
        return suspendCoroutine { continuation ->
            AppLinkData.fetchDeferredAppLinkData(getApplication()) {
                continuation.resume(it?.targetUri)
            }
        }
    }

    private fun sendOneSignal(deep: Any) {
        OneSignal.sendTag("key2", deep.toString().replace("myapp://", "").substringBefore("/"))
    }

    private fun sendOneSignal(dataAF: MutableMap<String, Any>?) {
        val campaign = dataAF?.get("campaign").toString()

        if (campaign != "null") {
            OneSignal.sendTag("key2", campaign.substringBefore("_"))
        } else {
            OneSignal.sendTag("key2", "organic")
        }
    }

}