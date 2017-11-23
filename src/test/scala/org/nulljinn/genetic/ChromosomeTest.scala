package org.nulljinn.genetic

import org.scalacheck.{Gen => SCGen}

class ChromosomeTest extends TestsBase {
  "Chromosome" when {
    "decode first zygote with dominant genes" should {
      "always override recessive genes of second  zygote" in {
        Chromosome("DDdd", "RrRr").decodeGenotype mustBe toBoolArray("1100")
      }
      "always override dominant genes of second  zygote" in {
        Chromosome("DDdd", "DdDd").decodeGenotype mustBe toBoolArray("1100")
      }
    }
    "decode first zygote with recessive genes" should {
      "always override recessive genes of second  zygote" in {
        Chromosome("RRrr", "RrRr").decodeGenotype mustBe toBoolArray("1100")
      }
      "always be overridden by dominant genes of second zygote" in {
        Chromosome("RRrr", "DdDd").decodeGenotype mustBe toBoolArray("1010")
      }
    }
    "cross zygotes" should {
      "swap 3 genes starting from pos 2" in {
        Chromosome("dddd dddd", "rrrr rrrr").crossZygotes(2, 3).toString mustBe Chromosome("dddr rrdd", "rrrd ddrr").toString
      }
      "swap whole right part of chromosome if num of genes to move bigger than size of chromosome" in forAll(SCGen.posNum[Int]) {
        (pos: Int) =>
          Chromosome("dddd dddd", "rrrr rrrr").crossZygotes(3, 5 + pos).toString mustBe Chromosome("rrrr rddd", "dddd drrr").toString
      }
    }
    "cross chromosomes" should {
      "cross dominant zygote for first parent with dominant zygote of second one and recessive zygote of first one with recessive zygote of second one" in {
        val firstParent = Chromosome("dddd dddd", "rrrr rrrr")
        val secondParent = Chromosome("DDDD DDDD", "RRRR RRRR")
        firstParent.crossChromosomes(secondParent, 1, 2).toString mustBe Chromosome("dddd dDDd", "rrrr rRRr").toString
      }
    }
    "mutate" should {
      "change gen in defined position of dominant zygote" in {
        Chromosome("dddd dddd", "rrrr rrrr").mutate(2, Gen.R1).toString mustBe Chromosome("dddd dRdd", "rrrr rrrr").toString
      }
    }
    "toString" should {
      "return strings concatenation of apply method params" in {
        Chromosome("dDrR rrrr", "RrDd dddd").toString mustBe "dDrR rrrr\nRrDd dddd"
      }
    }
  }

  def toBoolArray(str: String): Array[Boolean] = str.map {
    case '0' => false
    case '1' => true
  }.toArray.reverse
}
