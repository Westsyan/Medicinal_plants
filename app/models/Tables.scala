package models
// AUTO-GENERATED Slick data model
/** Stand-alone Slick data model for immediate use */
object Tables extends {
  val profile = slick.jdbc.MySQLProfile
} with Tables

/** Slick data model trait for extension, choice of backend or usage in the cake pattern. (Make sure to initialize this late.) */
trait Tables {
  val profile: slick.jdbc.JdbcProfile
  import profile.api._
  import com.github.tototoshi.slick.MySQLJodaSupport._
  import org.joda.time.DateTime
  import slick.model.ForeignKeyAction
  // NOTE: GetResult mappers for plain SQL are only generated for tables where Slick knows how to map the types of all columns.
  import slick.jdbc.{GetResult => GR}

  /** DDL for all tables. Call .create to execute. */
  lazy val schema: profile.SchemaDescription = Gansi.schema ++ Roseus.schema
  @deprecated("Use .schema instead of .ddl", "3.0")
  def ddl = schema

  /** Entity class storing rows of table Gansi
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param geneid Database column geneid SqlType(VARCHAR), Length(255,true)
   *  @param chr Database column chr SqlType(TEXT)
   *  @param length Database column length SqlType(BIGINT)
   *  @param start Database column start SqlType(BIGINT)
   *  @param end Database column end SqlType(BIGINT)
   *  @param strand Database column strand SqlType(TEXT)
   *  @param swissprot Database column swissprot SqlType(TEXT)
   *  @param kegg Database column kegg SqlType(TEXT)
   *  @param cog Database column cog SqlType(TEXT)
   *  @param nr Database column nr SqlType(TEXT)
   *  @param pfam Database column pfam SqlType(TEXT)
   *  @param cds Database column cds SqlType(TEXT)
   *  @param pep Database column pep SqlType(TEXT) */
  final case class GansiRow(id: Int, geneid: String, chr: String, length: Long, start: Long, end: Long, strand: String, swissprot: String, kegg: String, cog: String, nr: String, pfam: String, cds: String, pep: String)
  /** GetResult implicit for fetching GansiRow objects using plain SQL queries */
  implicit def GetResultGansiRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[GansiRow] = GR{
    prs => import prs._
    GansiRow.tupled((<<[Int], <<[String], <<[String], <<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table gansi. Objects of this class serve as prototypes for rows in queries. */
  class Gansi(_tableTag: Tag) extends profile.api.Table[GansiRow](_tableTag, Some("medicinal_plants"), "gansi") {
    def * = (id, geneid, chr, length, start, end, strand, swissprot, kegg, cog, nr, pfam, cds, pep) <> (GansiRow.tupled, GansiRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(geneid), Rep.Some(chr), Rep.Some(length), Rep.Some(start), Rep.Some(end), Rep.Some(strand), Rep.Some(swissprot), Rep.Some(kegg), Rep.Some(cog), Rep.Some(nr), Rep.Some(pfam), Rep.Some(cds), Rep.Some(pep)).shaped.<>({r=>import r._; _1.map(_=> GansiRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column geneid SqlType(VARCHAR), Length(255,true) */
    val geneid: Rep[String] = column[String]("geneid", O.Length(255,varying=true))
    /** Database column chr SqlType(TEXT) */
    val chr: Rep[String] = column[String]("chr")
    /** Database column length SqlType(BIGINT) */
    val length: Rep[Long] = column[Long]("length")
    /** Database column start SqlType(BIGINT) */
    val start: Rep[Long] = column[Long]("start")
    /** Database column end SqlType(BIGINT) */
    val end: Rep[Long] = column[Long]("end")
    /** Database column strand SqlType(TEXT) */
    val strand: Rep[String] = column[String]("strand")
    /** Database column swissprot SqlType(TEXT) */
    val swissprot: Rep[String] = column[String]("swissprot")
    /** Database column kegg SqlType(TEXT) */
    val kegg: Rep[String] = column[String]("kegg")
    /** Database column cog SqlType(TEXT) */
    val cog: Rep[String] = column[String]("cog")
    /** Database column nr SqlType(TEXT) */
    val nr: Rep[String] = column[String]("nr")
    /** Database column pfam SqlType(TEXT) */
    val pfam: Rep[String] = column[String]("pfam")
    /** Database column cds SqlType(TEXT) */
    val cds: Rep[String] = column[String]("cds")
    /** Database column pep SqlType(TEXT) */
    val pep: Rep[String] = column[String]("pep")

    /** Primary key of Gansi (database name gansi_PK) */
    val pk = primaryKey("gansi_PK", (id, geneid))
  }
  /** Collection-like TableQuery object for table Gansi */
  lazy val Gansi = new TableQuery(tag => new Gansi(tag))

  /** Entity class storing rows of table Roseus
   *  @param id Database column id SqlType(INT), AutoInc
   *  @param geneid Database column geneid SqlType(VARCHAR), Length(255,true)
   *  @param chr Database column chr SqlType(TEXT)
   *  @param length Database column length SqlType(BIGINT)
   *  @param start Database column start SqlType(BIGINT)
   *  @param end Database column end SqlType(BIGINT)
   *  @param strand Database column strand SqlType(TEXT)
   *  @param go Database column go SqlType(TEXT)
   *  @param kegg Database column kegg SqlType(TEXT)
   *  @param kog Database column kog SqlType(TEXT)
   *  @param kogAnno Database column kog_anno SqlType(TEXT)
   *  @param swissprot Database column swissprot SqlType(TEXT)
   *  @param tremal Database column tremal SqlType(TEXT)
   *  @param nr Database column nr SqlType(TEXT)
   *  @param cds Database column cds SqlType(TEXT)
   *  @param pep Database column pep SqlType(TEXT) */
  final case class RoseusRow(id: Int, geneid: String, chr: String, length: Long, start: Long, end: Long, strand: String, go: String, kegg: String, kog: String, kogAnno: String, swissprot: String, tremal: String, nr: String, cds: String, pep: String)
  /** GetResult implicit for fetching RoseusRow objects using plain SQL queries */
  implicit def GetResultRoseusRow(implicit e0: GR[Int], e1: GR[String], e2: GR[Long]): GR[RoseusRow] = GR{
    prs => import prs._
    RoseusRow.tupled((<<[Int], <<[String], <<[String], <<[Long], <<[Long], <<[Long], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String], <<[String]))
  }
  /** Table description of table roseus. Objects of this class serve as prototypes for rows in queries. */
  class Roseus(_tableTag: Tag) extends profile.api.Table[RoseusRow](_tableTag, Some("medicinal_plants"), "roseus") {
    def * = (id, geneid, chr, length, start, end, strand, go, kegg, kog, kogAnno, swissprot, tremal, nr, cds, pep) <> (RoseusRow.tupled, RoseusRow.unapply)
    /** Maps whole row to an option. Useful for outer joins. */
    def ? = (Rep.Some(id), Rep.Some(geneid), Rep.Some(chr), Rep.Some(length), Rep.Some(start), Rep.Some(end), Rep.Some(strand), Rep.Some(go), Rep.Some(kegg), Rep.Some(kog), Rep.Some(kogAnno), Rep.Some(swissprot), Rep.Some(tremal), Rep.Some(nr), Rep.Some(cds), Rep.Some(pep)).shaped.<>({r=>import r._; _1.map(_=> RoseusRow.tupled((_1.get, _2.get, _3.get, _4.get, _5.get, _6.get, _7.get, _8.get, _9.get, _10.get, _11.get, _12.get, _13.get, _14.get, _15.get, _16.get)))}, (_:Any) =>  throw new Exception("Inserting into ? projection not supported."))

    /** Database column id SqlType(INT), AutoInc */
    val id: Rep[Int] = column[Int]("id", O.AutoInc)
    /** Database column geneid SqlType(VARCHAR), Length(255,true) */
    val geneid: Rep[String] = column[String]("geneid", O.Length(255,varying=true))
    /** Database column chr SqlType(TEXT) */
    val chr: Rep[String] = column[String]("chr")
    /** Database column length SqlType(BIGINT) */
    val length: Rep[Long] = column[Long]("length")
    /** Database column start SqlType(BIGINT) */
    val start: Rep[Long] = column[Long]("start")
    /** Database column end SqlType(BIGINT) */
    val end: Rep[Long] = column[Long]("end")
    /** Database column strand SqlType(TEXT) */
    val strand: Rep[String] = column[String]("strand")
    /** Database column go SqlType(TEXT) */
    val go: Rep[String] = column[String]("go")
    /** Database column kegg SqlType(TEXT) */
    val kegg: Rep[String] = column[String]("kegg")
    /** Database column kog SqlType(TEXT) */
    val kog: Rep[String] = column[String]("kog")
    /** Database column kog_anno SqlType(TEXT) */
    val kogAnno: Rep[String] = column[String]("kog_anno")
    /** Database column swissprot SqlType(TEXT) */
    val swissprot: Rep[String] = column[String]("swissprot")
    /** Database column tremal SqlType(TEXT) */
    val tremal: Rep[String] = column[String]("tremal")
    /** Database column nr SqlType(TEXT) */
    val nr: Rep[String] = column[String]("nr")
    /** Database column cds SqlType(TEXT) */
    val cds: Rep[String] = column[String]("cds")
    /** Database column pep SqlType(TEXT) */
    val pep: Rep[String] = column[String]("pep")

    /** Primary key of Roseus (database name roseus_PK) */
    val pk = primaryKey("roseus_PK", (id, geneid))
  }
  /** Collection-like TableQuery object for table Roseus */
  lazy val Roseus = new TableQuery(tag => new Roseus(tag))
}
