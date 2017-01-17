/*

    adam-snpeff  SnpEff on Spark via ADAM pipe APIs.
    Copyright (c) 2016 held jointly by the individual authors.

    This library is free software; you can redistribute it and/or modify it
    under the terms of the GNU Lesser General Public License as published
    by the Free Software Foundation; either version 3 of the License, or (at
    your option) any later version.

    This library is distributed in the hope that it will be useful, but WITHOUT
    ANY WARRANTY; with out even the implied warranty of MERCHANTABILITY or
    FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public
    License for more details.

    You should have received a copy of the GNU Lesser General Public License
    along with this library;  if not, write to the Free Software Foundation,
    Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307  USA.

    > http://www.fsf.org/licensing/licenses/lgpl.html
    > http://www.opensource.org/licenses/lgpl-license.php

*/
package com.github.heuermh.adam.snpeff

import org.apache.spark.{ SparkConf, SparkContext }
import org.bdgenomics.adam.models.VariantContext
import org.bdgenomics.adam.rdd.ADAMContext._
import org.bdgenomics.adam.rdd.variant.{ VariantContextRDD, VCFInFormatter, VCFOutFormatter }

/**
 * SnpEff on Spark via ADAM pipe APIs.
 * 
 * @author  Michael Heuer
 */
object AdamSnpEff {
  def main(args: Array[String]) {
    if (args.length < 2) {
      System.err.println("at least two arguments required, e.g. input.vcf and output.vcf")
      System.exit(1)
    }

    val conf = new SparkConf()
      .setAppName("ADAM SnpEff")
      .set("spark.serializer", "org.apache.spark.serializer.KryoSerializer")
      .set("spark.kryo.registrator", "org.bdgenomics.adam.serialization.ADAMKryoRegistrator")
      .set("spark.kryo.referenceTracking", "true")

    val sc = new SparkContext(conf)
    val input: VariantContextRDD = sc.loadVcf(args(0))

    implicit val tFormatter = VCFInFormatter
    implicit val uFormatter = new VCFOutFormatter(input.headerLines)

    val snpEffCommand = "snpEff -download GRCh38.82"
    val output: VariantContextRDD = input.pipe[VariantContext, VariantContextRDD, VCFInFormatter](snpEffCommand)
      .transform(_.cache())

    output.saveAsVcf(args(1))
    //output.saveAsVcf(args(1), asSingleFile = true)
  }
}
