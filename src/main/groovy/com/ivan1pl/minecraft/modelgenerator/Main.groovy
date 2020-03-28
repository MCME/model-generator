package com.ivan1pl.minecraft.modelgenerator

import groovy.ant.FileNameFinder
import groovy.cli.picocli.CliBuilder

/**
 * Main class.
 */
class Main {
    /**
     * Main function.
     * @param args command line arguments
     */
    static void main(args) {
        def cli = new CliBuilder(
                usage: 'java -jar model-generator.jar [options]',
                header: '''Available use cases:
* Generate block models:  java -jar model-generator.jar -o <dir> <file(s)>
* Display usage and quit: java -jar model-generator.jar -h''')
        cli.h(longOpt: 'help',                            'Show usage information')
        cli.o(longOpt: 'output', args: 1, argName: 'dir', 'Provide output directory')

        def options = cli.parse(args)
        if (!options) return
        def arguments = options.arguments()

        if (options.h || !options.o || !arguments) cli.usage()
        else {
            arguments.collect { arg -> new FileNameFinder().getFileNames('.', arg) }.flatten()
                    .collect { name -> new File(name) }
                    .each { f -> new ImageProcessor(f, new File(options.o)).process() }
        }
    }
}
