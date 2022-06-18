<?xml version="1.0"?>
<xsl:stylesheet xmlns:ili="http://www.interlis.ch/INTERLIS2.3" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output encoding="UTF-8" indent="yes" method="html"/>
    <xsl:template match="/">
        <html>
        <head>
        <title>foo • bar</title>
        <meta name="description" content="INTERLIS Repository Mirror"/>
        <meta name="author" content="Stefan Ziegler" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <meta http-equiv="cache-control" content="no-cache"/>

        <style>
            .level1 {
                font-weight: 700;
                background-color: yellow;
            }
            .level2 {
                font-weight: 400;
                background-color: blue;
                padding-left: 100px;
            }
            .level3 {
                font-weight: 400;
                background-color: wheat;
                padding-left: 100px;
            }
            .level4 {
                font-weight: 400;
                background-color: green;
                padding-left: 100px;
            }


        </style>

        </head>
        <body>

            <xsl:apply-templates select="ili:TRANSFER/ili:DATASECTION/ili:IliRepository20.RepositoryIndex" />

        </body>
        </html>
    </xsl:template>

    <xsl:template match="ili:IliRepository20.RepositoryIndex">  
        <xsl:for-each-group select="ili:IliRepository20.RepositoryIndex.ModelMetadata" group-by="tokenize(ili:File, '/')[1]">
            <xsl:sort order="ascending" select="tokenize(ili:File, '/')[1]"/>
                <details class="level1">
                    <summary>
                        <xsl:value-of select="tokenize(ili:File, '/')[1]"/>
                    </summary>
                    <p>
                        <xsl:for-each-group select="current-group()" group-by="tokenize(ili:File, '/')[2]">
                            <xsl:sort order="ascending" select="tokenize(ili:File, '/')[2]"/>
                            <details class="level2">
                                <summary>
                                    <xsl:value-of select="tokenize(ili:File, '/')[2]"/>
                                </summary>
                                <p>
                                    <!--Unterscheidung ILI vs Ordner hier?-->
                                    <xsl:for-each-group select="current-group()" group-by="tokenize(ili:File, '/')[3]">
                                        <xsl:sort order="ascending" select="tokenize(ili:File, '/')[3]"/>
                                        <details class="level3">
                                            <summary>
                                                <xsl:value-of select="tokenize(ili:File, '/')[3]"/>
                                            </summary>
                                            <p>
                                                <!--Unterscheidung ILI vs Ordner hier?-->
                                            </p>
                                        </details>
                                    </xsl:for-each-group>
                                </p>
                            </details>
                        </xsl:for-each-group>
                    </p>
                </details>
            <xsl:message>foo<xsl:value-of select="local-name()" /></xsl:message>
            <xsl:message>Hallo Welt.</xsl:message>
        </xsl:for-each-group>
    </xsl:template>


</xsl:stylesheet>


            <!--
                <xsl:value-of select="tokenize(., '/')"/>
                <xsl:variable name="articleArr" select="tokenize(.,'/')" />
                <br/>
                <xsl:for-each select="$articleArr">
                    <xsl:value-of select="." />
                    <br/>
                </xsl:for-each>
                                    <br/><br/>
-->

            <!-- <xsl:value-of select="current-group()" />

            <br/><br/>

            <xsl:message>foo<xsl:value-of select="." /></xsl:message> -->

            <!-- <xsl:variable name="filenameElements" select="tokenize(ili:File,'/')" />
            <xsl:message><xsl:value-of select="count($filenameElements)" /></xsl:message>
            <xsl:message><xsl:value-of select="$filenameElements[last()-1]" /></xsl:message>

            <xsl:variable name="pathElements" select="remove($filenameElements,last())" />
            <xsl:message>foo<xsl:value-of select="$pathElements[3]" /></xsl:message>

            <xsl:message>bar<xsl:value-of select="remove(tokenize(ili:File,'/'),last())[2]" /></xsl:message>
 -->


            <!-- <xsl:message><xsl:value-of select="count(tokenize(ili:File,'/'))" /></xsl:message>

            <xsl:message><xsl:value-of select="remove(tokenize(ili:File,'/'), count(tokenize(ili:File,'/')))" /></xsl:message>
            <xsl:message><xsl:value-of select="remove(tokenize(ili:File,'/'), last())" /></xsl:message>
            <xsl:message><xsl:value-of select="tokenize(ili:File,'/')" /></xsl:message>
            <xsl:message><xsl:value-of select="ili:File" /></xsl:message> -->
            <!-- <xsl:message><xsl:value-of select="." /></xsl:message> -->
