# Nuskope Usage Meter ( NUM )

[![GitHub stars](https://img.shields.io/github/stars/adamzerella/NuskopeUsageMeter.svg)](https://github.com/adamzerella/adamzerella/stargazers)
[![GitHub license](https://img.shields.io/github/license/adamzerella/NuskopeUsageMeter.svg)](https://github.com/adamzerella/adamzerella/blob/master/LICENSE)
[![GitHub issues](https://img.shields.io/github/issues/adamzerella/NuskopeUsageMeter.svg)](https://github.com/adamzerella/adamzerella/issues)

> <strong>NOTE: THIS IS A DEPRECIATED AND UNMAINTAINED PROJECT!  IT HAS ONLY HAD THE DOCUMENTATION AND PROJECT STRUCTURE REFACTORED SINCE INCEPTION.</strong>

> Internet usage meter for the ISP provider Nuskope, inspired by the ISP Internode's Monthly Usage Meter (MUM) tool.


# Prerequisites

- [openjdk >= 1.8](http://openjdk.java.net/install/)
- [openjdk SDK](http://openjdk.java.net/install/)


# Compile
```bash
mkdir bin/
cd src/

### Compile byte code
javac com/adamzerella/nuskopeusagemeter/*.java -d .

### ( Optional ) Generate documentation
javadoc com/adamzerella/nuskopeusagemeter/*.java -d ../doc/

### Bundle into jar
jar cvmf com/adamzerella/nuskopeusagemeter/META-INF/MANIFEST.MF ../bin/num.jar com/adamzerella/nuskopeusagemeter/*.class
```


# Start
```bash
java -jar ../bin/num.jar
```


# License

This project is licensed under the MIT License - see the [LICENSE](https://raw.githubusercontent.com/adamzerella/NuskopeUsageMeter/master/LICENSE) file for details.


# Contributors

<div style="display:inline;">
  <img width="64" height="64" href="https://github.com/adamzerella" src="https://avatars0.githubusercontent.com/u/1501560?s=460&v=4" alt="Adam A. Zerella"/>
</div>