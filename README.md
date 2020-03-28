# Model generator

A simple command line tool for generating item-like block models from images basing on transparent pixels.

## Building

Building is very simple. Just run (linux/unix):

```./gradlew jar```

or (windows):

```gradlew.bat jar```

## Usage

Simply run:

```java -jar <jar> -o <output dir> <image(s)>```

where `<jar>` is the built project jar, by default placed in `build/libs`. You can provide as many images as you like and you can use ant-like patterns (like `path/to/some/**/directory/image_*.png`). Absolute paths are not supported, use paths relative to your current working directory. All matching files will be processed and generated models will be placed in `<output dir>` directory with the same name as the image they were generated from.

## Example

Assume unix-like environment, `~/Projects/model-generator/build/libs/model-generator-0.1.jar` as jar path and `~/.minecraft/resourcepacks/some_rp/assets/minecraft/textures/block/` as current working directory containing images `my_block_1.png`, `my_block_2.png` and `my_block_alt.png`.

```java -jar ~/Projects/model-generator/build/libs/model-generator-0.1.jar -o ../../models/block my_block_*```

The command above will produce files `my_block_1.json`, `my_block_2.json` and `my_block_alt.json` located in `~/.minecraft/resourcepacks/some_rp/assets/minecraft/models/block/`. Files are ready to be used as block models.