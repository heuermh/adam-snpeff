# adam-snpeff

###Hacking adam-snpeff

Install

 * JDK 1.8 or later, http://openjdk.java.net
 * Apache Maven 3.3.9 or later, http://maven.apache.org
 * SnpEff version 4.2 or later, http://snpeff.sourceforge.net

To build

    $ mvn install

###Running adam-snpeff using `spark-submit`

```bash
$ spark-submit \
    --master local[*] \
    --class com.github.heuermh.adam.snpeff.AdamSnpEff \
    target/adam-snpeff_2.10-0.19.1-SNAPSHOT.jar \
    input.vcf \
    output.vcf
```
