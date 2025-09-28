package art.flieda.advancedperks.utilities

import org.bukkit.persistence.PersistentDataAdapterContext
import org.bukkit.persistence.PersistentDataType
import java.nio.ByteBuffer
import java.util.UUID

private const val UUID_BYTE_SIZE = 16

class UuidPersistentDataType : PersistentDataType<ByteArray, UUID> {

    override fun getPrimitiveType(): Class<ByteArray> = ByteArray::class.java

    override fun getComplexType(): Class<UUID> = UUID::class.java

    override fun toPrimitive(
        complex: UUID,
        context: PersistentDataAdapterContext
    ): ByteArray = ByteBuffer.allocate(UUID_BYTE_SIZE)
        .putLong(complex.mostSignificantBits)
        .putLong(complex.leastSignificantBits)
        .array()

    override fun fromPrimitive(
        primitive: ByteArray,
        context: PersistentDataAdapterContext
    ): UUID {
        val wrappedByteBuffer = ByteBuffer.wrap(primitive)
        return UUID(wrappedByteBuffer.long, wrappedByteBuffer.long)
    }

}

val uuidPersistentDataType = UuidPersistentDataType()
