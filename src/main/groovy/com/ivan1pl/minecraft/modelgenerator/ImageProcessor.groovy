package com.ivan1pl.minecraft.modelgenerator

import javax.imageio.ImageIO

/**
 * Image file processor.
 */
class ImageProcessor {
    private final File source
    private final File outputDir
    Integer lastH = null
    Integer lastV = null

    ImageProcessor(File source, File outputDir) {
        this.source = source
        this.outputDir = outputDir
    }

    void process() {
        List<Box> horizontal = []
        List<Box> vertical = []
        def image = new BufferedImage(ImageIO.read(source))
        if (image.width != 32 || image.height != 32) {
            throw new IllegalArgumentException("Invalid image size: $source.absolutePath")
        }
        33.times { i ->
            lastH = lastV = null
            33.times { j ->
                int alphaV = image.getAlpha(i, 31-j)
                int alphaH = image.getAlpha(j, 31-i)
                if (alphaV > 0 && lastV == null) lastV = j
                else if (alphaV == 0 && lastV != null) {
                    vertical << Box.column(lastV, j, i)
                    lastV = null
                }
                if (alphaH > 0 && lastH == null) lastH = j
                else if (alphaH == 0 && lastH != null) {
                    horizontal << Box.row(lastH, j, i)
                    lastH = null
                }
            }
        }
        generateModel(horizontal, vertical)
    }

    void generateModel(List<Box> horizontal, List<Box> vertical) {
        String textureName = source.name[0..<source.name.lastIndexOf('.')]
        List<String> elements = []
        elements.addAll(vertical.collect { box ->
            """\
        {   "from": [ ${box.fromX/2}, ${box.fromY/2}, $box.fromZ ],
            "to":   [ ${box.toX/2}, ${box.toY/2}, $box.toZ ],
            "shade": false,
            "faces": {
                "down": { "uv": [ ${box.fromX/2}, ${16-(box.fromY+1)/2}, ${box.toX/2}, ${16-box.fromY/2} ], "texture": "#texture" },
                "up":   { "uv": [ ${box.fromX/2}, ${16-box.toY/2}, ${box.toX/2}, ${16-(box.toY-1)/2} ], "texture": "#texture" }
            }
        }"""
        })
        elements.addAll(horizontal.collect {box ->
            """\
        {   "from": [ ${box.fromX/2}, ${box.fromY/2}, $box.fromZ ],
            "to":   [ ${box.toX/2}, ${box.toY/2}, $box.toZ ],
            "shade": false,
            "faces": {
                "west":  { "uv": [ ${box.fromX/2}, ${16-box.toY/2}, ${(box.fromX+1)/2}, ${16-box.fromY/2} ], "texture": "#texture" },
                "east":  { "uv": [ ${(box.toX-1)/2}, ${16-box.toY/2}, ${box.toX/2}, ${16-box.fromY/2} ], "texture": "#texture" },
                "south": { "uv": [ ${box.fromX/2}, ${16-box.toY/2}, ${box.toX/2}, ${16-box.fromY/2} ], "texture": "#texture" },
                "north": { "uv": [ ${box.toX/2}, ${16-box.toY/2}, ${box.fromX/2}, ${16-box.fromY/2} ], "texture": "#texture" }
            }
        }"""
        })
        /*elements.add(
                """\
        {   "from": [ -8, -8, 7 ],
            "to": [ 24, 24, 9 ],
            "shade": false,
            "faces": {
                "north": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" },
                "south": { "uv": [ 0, 0, 16, 16 ], "texture": "#texture" }
            }
        }"""
        )*/
        String model = """\
{
    "ambientocclusion": false,
    "display": {
        "thirdperson_righthand": { "scale": [ 0.5, 0.5, 1 ] },
        "thirdperson_lefthand":  { "scale": [ 0.5, 0.5, 1 ] },
        "firstperson_righthand": { "scale": [ 0.5, 0.5, 1 ] },
        "firstperson_lefthand":  { "scale": [ 0.5, 0.5, 1 ] },
        "gui":                   { "scale": [ 0.5, 0.5, 1 ] },
        "head":                  { "scale": [ 0.5, 0.5, 1 ] },
        "ground":                { "scale": [ 0.5, 0.5, 1 ] },
        "fixed":                 { "scale": [ 0.5, 0.5, 1 ] }
    },
    "textures": {
        "particle": "block/$textureName",
        "texture": "block/$textureName"
    },
    "elements": [
${elements.join(",\n")}
    ]
}
"""
        def modelFile = new File(outputDir, "${textureName}.json")
        if (modelFile.exists()) {
            modelFile.delete()
        }
        modelFile.createNewFile()
        modelFile.bytes = model.bytes
    }
}
