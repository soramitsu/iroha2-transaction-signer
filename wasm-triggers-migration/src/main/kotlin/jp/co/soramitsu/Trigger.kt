package jp.co.soramitsu

import jp.co.soramitsu.iroha2.generated.Duration
import jp.co.soramitsu.iroha2.generated.datamodel.account.AccountId
import jp.co.soramitsu.iroha2.generated.datamodel.events.EventsFilterBox
import jp.co.soramitsu.iroha2.generated.datamodel.events.data.events.account.AccountEventFilter
import jp.co.soramitsu.iroha2.generated.datamodel.events.data.filters.OriginFilterAccountEvent
import jp.co.soramitsu.iroha2.transaction.EntityFilters
import jp.co.soramitsu.iroha2.transaction.EventFilters
import jp.co.soramitsu.iroha2.transaction.Filters
import java.math.BigInteger
import java.util.Date

enum class RepeatsEnum(val num: Int) {
    INDEFINITELY(0),
    EXACTLY(1);

    companion object {
        fun from (num: Int): RepeatsEnum {
            return when (num) {
                INDEFINITELY.num -> INDEFINITELY
                EXACTLY.num -> EXACTLY
                else -> throw RuntimeException("Repeats $num not supported")
            }
        }
    }
}

enum class TriggerType(val num: Int) {
    TIME(0),
    DATA_BY_ACCOUNT_METADATA(1);

    companion object {
        fun from (num: Int): TriggerType {
            return when (num) {
                TIME.num -> TIME
                DATA_BY_ACCOUNT_METADATA.num -> DATA_BY_ACCOUNT_METADATA
                else -> throw RuntimeException("Trigger type $num not supported")
            }
        }
    }
}

fun getTimeTrigger(timeInterval: Long): EventsFilterBox.Time {
    val currentTime = Date().time / 1000
    return Filters.time(
        EventFilters.timeEventFilter(
            Duration(BigInteger.valueOf(currentTime), 0),
            Duration(BigInteger.valueOf(timeInterval), 0)
        )
    )
}

fun getDataTriggerByAccountMetadataInserted(accountId: AccountId? = null): EventsFilterBox.Data {
    var accountEvent: OriginFilterAccountEvent? = null
    if (accountId != null) {
        accountEvent = OriginFilterAccountEvent(accountId)
    }
    return Filters.data(
        EntityFilters.byAccount(
            idFilter = accountEvent,
            eventFilter = AccountEventFilter.ByMetadataInserted()
        )
    )
}
