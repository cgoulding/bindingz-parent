/*
 * Copyright (c) 2019 Connor Goulding
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.monadiccloud.bindingz.contract.plugin.gradle.providers

import org.jsonschema2pojo.*
import org.jsonschema2pojo.rules.RuleFactory

import java.nio.file.Paths

class JsonSchema2PojoConfiguration implements GenerationConfig {
    File targetSourceDirectory
    File targetResourceDirectory

    String providerName
    String contractName
    String version

    String packageName
    String className

    boolean generateBuilders = false
    boolean useInnerClassBuilders = false
    boolean usePrimitives = false
    char[] propertyWordDelimiters = ['-', ' ', '_'] as char[]
    boolean useLongIntegers = false
    boolean useBigIntegers = false
    boolean useDoubleNumbers = true
    boolean useBigDecimals = false
    boolean includeHashcodeAndEquals = true
    boolean includeConstructors = false
    boolean constructorsRequiredPropertiesOnly = false
    boolean includeToString = true
    String[] toStringExcludes = [] as String[]
    AnnotationStyle annotationStyle = AnnotationStyle.JACKSON
    boolean useTitleAsClassname = false
    InclusionLevel inclusionLevel = InclusionLevel.NON_NULL
    Class<? extends Annotator> customAnnotator = NoopAnnotator.class
    Class<? extends RuleFactory> customRuleFactory = RuleFactory.class
    boolean includeJsr303Annotations = false
    boolean includeJsr305Annotations = false
    boolean useOptionalForGetters = false
    SourceType sourceType = SourceType.JSONSCHEMA
    String outputEncoding = 'UTF-8'
    boolean useJodaDates = false
    boolean useJodaLocalDates = false
    boolean useJodaLocalTimes = false
    String dateTimeType = null
    String dateType = null
    String timeType = null
    boolean parcelable = false
    boolean serializable = false
    FileFilter fileFilter = new AllFileFilter()
    boolean initializeCollections = true
    String classNamePrefix = ''
    String classNameSuffix = ''
    String[] fileExtensions = [] as String[]
    boolean includeAdditionalProperties = true
    boolean includeGetters = true
    boolean includeSetters = true
    String targetVersion = '1.6'
    boolean includeDynamicAccessors = false
    boolean includeDynamicGetters = false
    boolean includeDynamicSetters = false
    boolean includeDynamicBuilders = false
    boolean formatDates = false
    boolean formatTimes = false
    boolean formatDateTimes = false
    String refFragmentPathDelimiters = "#/."
    SourceSortOrder sourceSortOrder = SourceSortOrder.OS
    Map<String, String> formatTypeMapping = Collections.emptyMap()
    boolean removeOldOutput = false
    String customDatePattern
    String customTimePattern
    String customDateTimePattern
    Language targetLanguage = Language.JAVA

    JsonSchema2PojoConfiguration(File targetSourceDirectory, File targetResourceDirectory, String providerName, String contractName, String version, String packageName, String className) {
        this.targetSourceDirectory = targetSourceDirectory
        this.targetResourceDirectory = targetResourceDirectory
        this.providerName = providerName
        this.contractName = contractName
        this.version = version
        this.packageName = packageName
        this.className = className
    }

    @Override
    Iterator<URL> getSource() {
        return Arrays.asList(Paths.get(targetResourceDirectory.toString(), providerName, contractName, version, className).toUri().toURL()).iterator()
    }

    @Override
    File getTargetDirectory() {
        return targetSourceDirectory
    }

    @Override
    String getTargetPackage() {
        return packageName
    }

    void setAnnotationStyle(String style) {
        annotationStyle = AnnotationStyle.valueOf(style.toUpperCase())
    }

    void setInclusionLevel(String level) {
        inclusionLevel = InclusionLevel.valueOf(level.toUpperCase())
    }

    void setCustomAnnotator(String clazz) {
        customAnnotator = this.setCustomAnnotator(Class.forName(clazz, true, this.class.classLoader))
    }

    void setCustomAnnotator(Class clazz) {
        customAnnotator = clazz
    }

    void setCustomRuleFactory(String clazz) {
        customRuleFactory = this.setCustomRuleFactory(Class.forName(clazz, true, this.class.classLoader))
    }

    void setCustomRuleFactory(Class clazz) {
        customRuleFactory = clazz
    }

    void setSourceType(String s) {
        sourceType = SourceType.valueOf(s.toUpperCase())
    }

    void setSourceSortOrder(String sortOrder) {
        sourceSortOrder = SourceSortOrder.valueOf(sortOrder.toUpperCase())
    }

    void setTargetLangauge(String language) {
        targetLangauge = Langauge.valueOf(language.toUpperCase())
    }

    boolean isFormatDateTimes() {
        return formatDateTimes;
    }
}
