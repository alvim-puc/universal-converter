# Universal File Converter - Implementation Plan

## Goal
Create a Maven project with modular, semi-hexagonal architecture for a universal file converter. The main class orchestrates conversion based on YAML config (source, target, type). Extensible converter implementations.

## Architecture

```
src/main/java/com/universalconverter/
├── core/                    # Domain layer (interfaces)
│   ├── Converter.java       # Interface: convert(InputStream, OutputStream, Config)
│   ├── ConversionConfig.java# Config DTO (source, target, type, options)
│   └── ConverterRegistry.java# Registry for converter lookup by type
├── converter/               # Adapter implementations
│   ├── CsvToJsonConverter.java
│   ├── JsonToCsvConverter.java
│   ├── XmlToJsonConverter.java
│   ├── YamlToJsonConverter.java
│   ├── YamlToTomlConverter.java
│   └── TomlToYamlConverter.java
├── config/                  # Configuration parsing
│   └── YamlConfigLoader.java# Loads ConversionConfig from YAML file
└── cli/                     # Application layer (orchestrator)
    └── Main.java            # Reads config, resolves converter, executes
```

## Key Decisions

1. **Single Maven module** with package separation (core, converter, config, cli)
2. **SPI/Registry pattern**: Converters register themselves; Main looks up by type string
3. **YAML Config structure**:
```yaml
source: "input.csv"
target: "output.json"
type: "csv-to-json"
options:
  delimiter: ","
  header: true
```
4. **Dependencies**: SnakeYAML (config), Jackson (JSON, CSV, XML, YAML), Apache Commons CSV, JAXB (XML), **Toml4j (TOML)**, **SLF4J + Logback (logging)**
5. **Tests**: Unit tests for each converter + integration test for Main

## Tasks

### 1. Project Setup (via Maven CLI - no subfolder)
- [ ] Create directory structure: `mkdir -p src/main/java/com/universalconverter/{core,converter,config,cli} src/test/java/com/universalconverter/{core,converter,config,cli} src/main/resources`
- [ ] Create minimal `pom.xml` at repo root with:
  - `groupId=com.universalconverter`, `artifactId=universal-converter`, `version=1.0.0`, `packaging=jar`
  - `maven-compiler-plugin` source/target 21
  - `maven-shade-plugin` for fat JAR with Main-Class
  - `maven-jar-plugin` for manifest
- [ ] Add dependencies via Maven CLI (requires existing pom.xml):
  - `mvn dependency:add -DgroupId=org.yaml -DartifactId=snakeyaml -Dversion=2.3 -Dscope=compile`
  - `mvn dependency:add -DgroupId=com.fasterxml.jackson.core -DartifactId=jackson-databind -Dversion=2.17.0 -Dscope=compile`
  - `mvn dependency:add -DgroupId=com.fasterxml.jackson.dataformat -DartifactId=jackson-dataformat-csv -Dversion=2.17.0 -Dscope=compile`
  - `mvn dependency:add -DgroupId=com.fasterxml.jackson.dataformat -DartifactId=jackson-dataformat-xml -Dversion=2.17.0 -Dscope=compile`
  - `mvn dependency:add -DgroupId=com.fasterxml.jackson.dataformat -DartifactId=jackson-dataformat-yaml -Dversion=2.17.0 -Dscope=compile`
  - `mvn dependency:add -DgroupId=org.apache.commons -DartifactId=commons-csv -Dversion=1.12.0 -Dscope=compile`
  - `mvn dependency:add -DgroupId=jakarta.xml.bind -DartifactId=jakarta.xml.bind-api -Dversion=4.0.0 -Dscope=compile`
  - `mvn dependency:add -DgroupId=org.glassfish.jaxb -DartifactId=jaxb-runtime -Dversion=4.0.0 -Dscope=runtime`
  - `mvn dependency:add -DgroupId=com.moandjiezana.toml -DartifactId=toml4j -Dversion=0.7.2 -Dscope=compile`
  - `mvn dependency:add -DgroupId=org.slf4j -DartifactId=slf4j-api -Dversion=2.0.9 -Dscope=compile`
  - `mvn dependency:add -DgroupId=ch.qos.logback -DartifactId=logback-classic -Dversion=1.5.6 -Dscope=compile`
  - `mvn dependency:add -DgroupId=org.junit.jupiter -DartifactId=junit-jupiter -Dversion=5.10.0 -Dscope=test`
- [ ] Verify `mvn clean package` produces executable JAR at repo root

### 2. Core Domain
- [ ] `Converter` interface
- [ ] `ConversionConfig` record/POJO
- [ ] `ConverterRegistry` (singleton, thread-safe)

### 3. Config Module
- [ ] `YamlConfigLoader` using SnakeYAML
- [ ] Unit test for config loading

### 4. Converter Implementations (Phase 1: CSV↔JSON)
- [ ] `CsvToJsonConverter` (Apache Commons CSV + Jackson)
- [ ] `JsonToCsvConverter`
- [ ] Unit tests for both

### 5. Converter Implementations (Phase 2: XML/YAML/TOML)
- [ ] `XmlToJsonConverter` (JAXB + Jackson)
- [ ] `YamlToJsonConverter` (SnakeYAML + Jackson)
- [ ] `YamlToTomlConverter` (SnakeYAML + Toml4j)
- [ ] `TomlToYamlConverter` (Toml4j + SnakeYAML)
- [ ] Unit tests

### 6. CLI Orchestrator
- [ ] `Main` class: args[0] = config.yaml path
- [ ] Load config → registry.get(type) → converter.convert()
- [ ] Error handling with clear messages

### 7. Integration Test
- [ ] End-to-end test: temp files + config → verify output

### 8. Verification
- [ ] `mvn clean package` succeeds
- [ ] `java -jar target/universal-converter-1.0.0.jar config.yaml` works
- [ ] Dependencies in target/dependency/ (for offline demo)

## Demo / Plan B
- Screenshots of successful build and run
- Short video recording of `mvn clean package` + execution
- Pre-built JAR committed to `demo/` folder as backup

## Open Questions
- [ ] Additional converters to prioritize beyond CSV↔JSON, XML↔JSON, YAML↔JSON, YAML↔TOML?