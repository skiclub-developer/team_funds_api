package service

import de.pengelkes.jooq.model.tables.Users
import de.pengelkes.jooq.model.tables.records.UsersRecord
import org.jooq.Record

class UserService private constructor() {
    private object Holder {
        val OBJECT = UserService()
    }

    companion object {
        val instance: UserService by lazy { Holder.OBJECT }
    }

    fun updateUser(name: String, record: UsersRecord) {
        Jooq.instance.update(Users.USERS)
                .set(record)
                .where(Users.USERS.NAME.eq(name))
                .execute()
    }

    fun getByName(name: String): UsersRecord? {
        val record = Jooq.instance.select().from(Users.USERS).where(Users.USERS.NAME.eq(name)).fetchOne()
        if (record != null) {
            return record.into(UsersRecord::class.java)
        }

        return null
    }
}