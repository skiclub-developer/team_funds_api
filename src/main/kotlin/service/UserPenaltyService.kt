package service

import de.pengelkes.jooq.model.tables.Penalties.PENALTIES
import de.pengelkes.jooq.model.tables.UserPenalties.USER_PENALTIES
import de.pengelkes.jooq.model.tables.records.UserPenaltiesRecord
import java.sql.Timestamp

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
            return result.into(UserPenaltiesRecord())
        }

        return null
    }

    fun getPenaltiesByUser(userId: Int): List<UserPenaltyListModel> {
        val result = mutableListOf<UserPenaltyListModel>()
        val records = Jooq.instance.select().from(USER_PENALTIES)
                .join(PENALTIES).onKey()
                .where(USER_PENALTIES.USER_ID.eq(userId))
                .orderBy(USER_PENALTIES.PENALTY_ID, USER_PENALTIES.CREATED_AT.desc())
                .fetch()

        records.forEach {
            result.add(UserPenaltyListModel(
                    it.getValue(USER_PENALTIES.AMOUNT),
                    it.getValue(USER_PENALTIES.CREATED_AT),
                    it.getValue(USER_PENALTIES.CHANGED_BY),
                    it.getValue(PENALTIES.PENALTY_NAME)
            ))
        }

        return result
    }

    fun createUserPenalty(userId: Int, penaltyId: Int, amount: Int, auditUser: String) {
        Jooq.instance.insertInto(USER_PENALTIES)
                .set(USER_PENALTIES.AMOUNT, amount)
                .set(USER_PENALTIES.USER_ID, userId)
                .set(USER_PENALTIES.PENALTY_ID, penaltyId)
                .set(USER_PENALTIES.CREATED_AT, Timestamp(System.currentTimeMillis()))
                .set(USER_PENALTIES.CHANGED_BY, auditUser)
                .execute()
    }
}

data class UserPenaltyListModel(val amount: Int, val createdAt: Timestamp, val changedBy: String, val penaltyName: String)