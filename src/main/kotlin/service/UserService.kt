package service

import de.pengelkes.jooq.model.tables.Users
import de.pengelkes.jooq.model.tables.Users.*
import de.pengelkes.jooq.model.tables.records.UsersRecord
import org.jooq.Record
import org.jooq.TableField

class UserService private constructor() {
    private object Holder {
        val OBJECT = UserService()
    }

    companion object {
        val instance: UserService by lazy { Holder.OBJECT }
    }

    fun updateUser(name: String, record: UsersRecord) {
        Jooq.instance.update(USERS)
                .set(record)
                .where(USERS.NAME.eq(name))
                .execute()
    }

    fun pay(name: String, amount: Int, field: TableField<UsersRecord, Int>) {
        Jooq.instance.update(USERS)
                .set(field, field - amount)
                .where(USERS.NAME.eq(name))
                .execute()
    }

    fun getByName(name: String): UsersRecord? {
        val record = Jooq.instance.select().from(USERS).where(USERS.NAME.eq(name)).fetchOne()
        if (record != null) {
            return record.into(UsersRecord::class.java)
        }

        return null
    }
}