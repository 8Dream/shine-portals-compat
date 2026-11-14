# shine-portals-compat

Mod NeoForge 1.21.1 feito em 14/11/2026 para a instancia `1.21.1` do Prism na Spark.

**Problema:** o Shine 2.0.2 (`ProgramMixin`) e o Immersive Portals 6.0.7 (`MixinProgram`) usam `@Redirect` na mesma chamada
(`IOUtils.toString` dentro de `Program.compileShaderInternal`). So um pode vencer e o jogo fechava ao abrir
("@Redirect conflict ... Critical injection failure").

**Solucao:**
1. `ProgramMixin` retirado do `shine.mixins.json` dentro do jar do Shine (original guardado em
   `~/EtherLake/serviços/moonshine/testes-e-logs/mods-removidos-20261114/shine-2.0.2+1.21.1-neoforge.jar.original`).
2. Este mod deixa o `@Redirect` com o Immersive Portals e aplica `BloomShaderInjection.transformVanillaShader` do Shine
   logo depois, no texto ja lido (`@ModifyVariable` na variavel local 5).

**Compilar:** `./compilar.sh` (Java 21 + bibliotecas do proprio Prism, sem Gradle) e copiar `build/*.jar` para a pasta mods.

**Atencao:** atualizar o Shine desfaz o passo 1 e o crash volta.

## 1.1.0 (14/11/2026)
Tambem protege o WorldEditCUI (`WorldEditCuiTickGuardMixin`): o CUI so cria o `controller` no `FMLClientSetupEvent`,
mas escuta o tick do cliente desde o inicio. Com ~300 mods o tick vem antes do fim do setup e o jogo caia com
`NullPointerException ... "this.controller" is null`. O tick do CUI e pulado ate o controller existir.
O nome do mod ficou `shine_portals_compat` para nao trocar de identidade.
