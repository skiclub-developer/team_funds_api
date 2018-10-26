package service

import de.pengelkes.jooq.model.tables.UserPenalties
import de.pengelkes.jooq.model.tables.UserPenalties.*
import de.pengelkes.jooq.model.tables.records.UserPenaltiesRecord
import org.jooq.Record

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
        Jooq.instance.insertInto(USER_PENALTIES)
                .set(USER_PENALTIES.AMOUNT, amount)
                .set(USER_PENALTIES.PENALTY_ID, penaltyId)
                .set(USER_PENALTIES.USER_ID, userId)
                .execute()
    }

    fun updateUserPenaltyAmount(userPenaltyRecord: UserPenaltiesRecord, amount: Int) {
        userPenaltyRecord.amount = userPenaltyRecord.amount + amount
        Jooq.instance.update(USER_PENALTIES)
                .set(userPenaltyRecord)
                .where(USER_PENALTIES.USER_ID.eq(userPenaltyRecord.userId))
                .and(USER_PENALTIES.PENALTY_ID.eq(userPenaltyRecord.penaltyId))
                .execute()
    }
}