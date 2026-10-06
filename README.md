# Universal File Converter

Converte arquivos usando um tipo e opções definidos em YAML. Os caminhos podem ficar no YAML para execuções repetíveis ou ser informados no CLI para cada chamada.

```bash
java -jar target/universal-converter-1.0.0.jar config.yaml [input-file] [output-file]
```

Prioridade dos caminhos: `input-file` e `output-file` do CLI substituem `source` e `target` do YAML. Assim, um `target` no YAML é a saída padrão. Quando um caminho não for passado no CLI, ele precisa estar definido no YAML.

Exemplo de `config.yaml`:

```yaml
target: "output.json" # saída padrão
type: "csv-to-json"
options:
  delimiter: ","
  header: true
```

```bash
# usa input.csv e a saída padrão output.json
java -jar target/universal-converter-1.0.0.jar config.yaml input.csv

# substitui os dois caminhos
java -jar target/universal-converter-1.0.0.jar config.yaml input.csv resultado.json
```

Tipos disponíveis: `csv-to-json`, `json-to-csv`, `xml-to-json`, `yaml-to-json`, `yaml-to-toml`, `toml-to-yaml`.
