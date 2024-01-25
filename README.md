The Lord Of The Rings
==================

This is the repository for the [The Lord Of The Rings] app.

It is a **work in progress** 🚧.

**The Lord Of The Rings** is a read only functional Android app built entirely with Kotlin and Jetpack Compose. It
follows Android design and development best practices and is intended to be a prototype for a future
Fully functioning The Lord Of The Rings Application

The app is currently in development.

# Features

**The Lord Of The Rings** displays content from the
[The One Api](https://the-one-api.dev/).

currently only characters are listed.

# Development Environment

**The Lord Of The Rings** uses the Gradle build system and can be imported directly into
Android Studio (It was developed using the following Android Studio Version).

```
Android Studio Hedgehog | 2023.1.1 Patch 2
Build #AI-231.9392.1.2311.11330709, built on January 18, 2024
Runtime version: 17.0.7+0-17.0.7b1000.6-10550314 aarch64
VM: OpenJDK 64-Bit Server VM by JetBrains s.r.o.
macOS 14.2.1
GC: G1 Young Generation, G1 Old Generation
Memory: 8192M
Cores: 16
Metal Rendering is ON
Registry:
    external.system.auto.import.disabled=true
    debugger.new.tool.window.layout=true
    ide.text.editor.with.preview.show.floating.toolbar=false
    ide.instant.shutdown=false
    ide.experimental.ui=true

```

# Description

The MVI application employs the following

* Ktor client for remote restful api calls
* Arrow-kt for Either error handling
* Room for local persistence
* Compose for all ui
* Dagger/Hilt for DI
* Firebase crashlytics/analytics
* JUnit 4/ Robolectric for unit testing
* Cash App molecule library/plugin for MVI

# Future Improvements

modularise the application as follows:-

* app
* background (*database*, *workers*, *network*)
* shared (*mvi*, *covert*, *shared*)
* feature (*character*, *quote*, *book*, *movie*)

* database sub module contains sqlite dao's and the database definition
* workers sub module contains android work manager background workers that synchronise
all remote data into the local database. there is one worker for each data type available,
e.g. characterWorker, quoteWorker etc..
* network sub module contains the ktor client configuration that exposes the api endpoints

* mvi sub module contains the molecule related base definitions
* covert sub module supports local data encryption and employs the google tink library
* shared sub module contains all common resources required by the rest of the application, 
e.g. the data model classes for network, database and ui, also any miscellaneous extension functions,
data model mappers (e.g. `org.mapstruct`), it also will contain resources such as any required room
database type converters as well as any required `androidx.startup` initialisers etc..

* character sub module contains all compose, di, viewmodel etc code to display chara


# Testing

theres more pure unit tests required.
i always attempt to **only** use pure junits as instrumented tests perform very badly and
take too much time and resources, especially when they are executed as part of the CI/CD pipeline.

