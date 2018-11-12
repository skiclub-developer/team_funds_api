package service

import de.pengelkes.jooq.model.tables.UserPenalties.USER_PENALTIES
import de.pengelkes.jooq.model.tables.records.UserPenaltiesRecord
import java.sql.Date

class UserPenaltyService private constructor() {
    private object Holder {
        val OBJECT = UserPenaltyService()
    }

    companion object {
        val instance: UserPenaltyService by lazy { Holder.OBJECT }
    }

    fun getUserPenalty(userId: Int, penaltyId: Int): UserPenaltiesRecord? {
        val result = Jooq.instance.select().from(USER_PENALTIES)
                .where(USER_PENALTIES.USER_ID.eq(userId))
                .and(USER_PENALTIES.PENALTY_ID.eq(penaltyId)).fetchOne()
        if (result != null) {
            return result.into(UserPenaltiesRecord::class.java)
        }

        return null
    }

    fun createUserPenalty(userId: Int, penaltyId: Int, amount: Int) {
        val userPenalties = UserPenaltiesRecord()
        userPenalties.amount = amount
        userPenalties.penaltyId = penaltyId
        userPenalties.userId = userId
        userPenalties.createdAt = Date(System.currentTimeMillis())

        userPenalties.store()
    }
}