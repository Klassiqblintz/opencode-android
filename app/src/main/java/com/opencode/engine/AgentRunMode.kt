package com.opencode.engine

/**
 * How each agent can run: on-device (pRoot Linux environment) or connecting to a remote server.
 */
enum class AgentRunMode {
    /**
     * Run on-device using pRoot or similar Linux emulation.
     * Requires root or a pre-configured Linux environment on the device.
     * All models run locally via llama.cpp.
     */
    ON_DEVICE,

    /**
     * Connect to a remote server via HTTP/WebSocket.
     * The remote server handles all computation.
     * Requires network connectivity.
     */
    REMOTE_SERVER,
}

/**
 * Where the model comes from: cloud API, local GGUF file, or both (hybrid).
 */
enum class ModelSource {
    /**
     * Cloud API — model runs on a remote server (Anthropic, OpenAI, DeepSeek, etc.)
     * Requires an API key and network connectivity.
     */
    CLOUD_API,

    /**
     * Local GGUF — model runs on-device via llama.cpp.
     * Requires a .gguf file downloaded or imported.
     */
    LOCAL_GGUF,

    /**
     * Hybrid — can switch between cloud API and local GGUF.
     * Falls back to local if no API key, or to cloud if local model isn't available.
     */
    HYBRID,
}
