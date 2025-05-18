package com.arthead.coinstatspredictor.infrastructure.adapters.userinterface;

import com.arthead.coinstatspredictor.infrastructure.ports.ExternalModelRunner;

public class PythonReader implements ExternalModelRunner {
    private final String datamartPath;
    private final ResultWriter resultWriter;

    public PythonReader(String csvResults, String datamart) {
        this.datamartPath = datamart;
        this.resultWriter = new ResultWriter(csvResults);
    }

    @Override
    public void runModel() throws Exception {
        String rutaScript = "coin-stats-predictor/src/main/resources/modeldacd.py";
        ProcessBuilder pb = new ProcessBuilder("python", rutaScript, datamartPath);
        pb.redirectErrorStream(true);
        Process proceso = pb.start();
        byte[] outputBytes = proceso.getInputStream().readAllBytes();
        proceso.waitFor();
        String output = new String(outputBytes);
        resultWriter.write(output);
    }
}
