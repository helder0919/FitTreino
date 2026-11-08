@Entity(tableName = "membro_b_table")
data class MembroBEntity(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val titulo: String,
    val descricao: String
)
