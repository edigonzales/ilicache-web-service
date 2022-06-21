<?xml version="1.0"?>
<xsl:stylesheet xmlns:ili="http://www.interlis.ch/INTERLIS2.3" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:output encoding="UTF-8" indent="yes" method="html"/>
    <xsl:template match="/">
        <html>
        <head>
        <title>INTERLIS Repository Mirror</title>
        <meta name="description" content="INTERLIS Repository Mirror"/>
        <meta name="author" content="Stefan Ziegler" />
        <meta name="viewport" content="width=device-width, initial-scale=1" />
        <meta http-equiv="cache-control" content="no-cache"/>

        <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=JetBrains+Mono&amp;family=Roboto:ital,wght@0,400;0,700;0,900;1,400&amp;display=swap"/>
        <link type="text/css" rel="stylesheet" href="https://fonts.googleapis.com/icon?family=Material+Icons"/>

        <style>
            html {
                font-family: 'Roboto', sans-serif;
                font-size: 16px;
                color: #333333;
            }

            body {
                margin: 0px;
                padding: 0px;
                background: #FFFFFF; 
            }

            #container {
                margin-left: auto;
                margin-right: auto;
                width: 1200px;
                max-width: 95%;
                background-color: #FFFFFF;        
            }

            .logo {
                margin-top: 20px;
                text-align: right;
                min-width: 50px;
            }

            #title {
                margin-top: 60px;
                margin-bottom: 30px;
                font-size: 40px;
                font-weight: 900;
            }

            .info {
                margin-top: 30px;
                margin-bottom: 30px;
                font-style: italic;
            }

        /*
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
          */  
            details {
                background-color: white;
                margin-top: 5px;
                margin-bottom: 15px;
            }

            details > details {
                background-color: white;
                padding-left: 20px;
                margin-bottom: 5px;
            }

            details > details > details {
                background-color: white;
                padding-left: 20px;
            }

            details > details > details > details {
                background-color: white;
                padding-left: 20px;
            }

            p {
                margin-block-end: 0px;
                margin-block-start: 0px;
            }

            summary a * {
                pointer-events: none;
            }

            table {
                border-collapse: collapse;
                border-spacing: 0;
                width: 100%;
                border: 0px solid #ddd;
                background-color: rgba(237, 237, 237, 0.3);
            }
            
            th, td {
                text-align: left;
                padding: 8px;
            } 

            a.default-link {
                overflow: hidden;
                text-overflow: ellipsis;
                color: #c62828; 
                text-decoration: none !important;
            }

            a.default-link:hover {
                color: #c62828;
                text-decoration: underline !important;
            }  

            a.default-link:visited {
                color: #c62828; 
                text-decoration: underline !important;
            }  

            a.black-link {
                overflow: hidden;
                text-overflow: ellipsis;
                color: #333333; 
                text-decoration: none !important;
            }

            a.black-link:hover {
                color: #333333;
                text-decoration: underline !important;
            }  

            a.black-link:visited {
                color: #333333; 
                text-decoration: underline !important;
            }  

            a.icon-link {
                overflow: hidden;
                text-overflow: ellipsis;
                color: #333333; 
                text-decoration: none !important;
            }

            a.icon-link:hover {
                color: #333333;
                text-decoration: none !important;
            }  

            a.icon-link:visited {
                color: #333333; 
                text-decoration: none !important;
            }  

            hr {
                border: none;
                height: 1px;
                background-color: #eee; 
            }

            .material-icons.md-18 { font-size: 18px; }
            .material-icons.md-24 { font-size: 24px; } /* Default */
            .material-icons.md-36 { font-size: 36px; }
            .material-icons.md-48 { font-size: 48px; }

        </style>

        </head>
        <body>
            <div id="container">
                <div class="logo">
                    <div>
                        <img src="logo/Logo.png" alt="Logo of organisation" style="max-width: 100%; min-width:200px;"/>
                    </div>
                </div>

                <div id="title">INTERLIS-Repositories</div>
                <div class="info">Last time cloned: </div>

                <xsl:apply-templates select="ili:TRANSFER/ili:DATASECTION/ili:IliRepository20.RepositoryIndex" />

            </div>
        </body>
        </html>
    </xsl:template>

    <xsl:template match="ili:IliRepository20.RepositoryIndex">  

    <!--fn:subsequent????-->
        <xsl:for-each-group select="ili:IliRepository20.RepositoryIndex.ModelMetadata" group-by="tokenize(ili:File, '/')[1]">
            <xsl:sort order="ascending" select="current-grouping-key()"/>
                <details class="level1">
                    <summary>
                        <span>
                            <xsl:value-of select="current-grouping-key()"/>
                        </span>
                        <xsl:text>&#160;</xsl:text>
                        <span>
                            <i>
                                <xsl:attribute name="class">material-icons</xsl:attribute>
                                <xsl:attribute name="style">vertical-align: -5px;</xsl:attribute>
                                <xsl:element name="a">
                                    <xsl:attribute name="class">
                                        <xsl:text>icon-link</xsl:text>
                                    </xsl:attribute>
                                    <xsl:attribute name="href">
                                        <xsl:text>https://</xsl:text><xsl:value-of select="current-grouping-key()"/>
                                    </xsl:attribute>
                                    <xsl:attribute name="target">
                                        <xsl:text>_blank</xsl:text>
                                    </xsl:attribute>
                                    launch
                                </xsl:element>
                            </i>
                        </span>
                    </summary>
                    <p>
                        <xsl:for-each-group select="current-group()" group-by="tokenize(ili:File, '/')[2]">
                            <xsl:sort order="ascending" select="current-grouping-key()"/>
                            <details class="level2">
                                <summary>
                                <!--if else... auch hier-->
                                    <xsl:value-of select="current-grouping-key()"/><xsl:text>/</xsl:text>
                                </summary>
                                <p>
                                    <xsl:for-each-group select="current-group()" group-by="tokenize(ili:File, '/')[3]">
                                        <xsl:sort order="ascending" select="current-grouping-key()"/>
                                        <details class="level3">
                                            <xsl:choose>
                                                <xsl:when test="ends-with(current-grouping-key(),'.ili')">
                                                    <summary>
                                                        <xsl:value-of select="ili:Name"/>
                                                    </summary>
                                                    <!-- p in when/otherwise, da auch unterschiedlich-->
                                                </xsl:when>
                                                <xsl:otherwise>
                                                    <summary>
                                                        <xsl:value-of select="current-grouping-key()"/>
                                                    </summary>
                                                </xsl:otherwise>
                                            </xsl:choose>
                                            <p>
                                                <!--Unterscheidung ILI vs Ordner hier?-->
                                                <xsl:for-each-group select="current-group()" group-by="tokenize(ili:File, '/')[4]">
                                                    <xsl:sort order="ascending" select="current-grouping-key()"/>
                                                    <details class="level4">
                                                        <summary>
                                                            <xsl:value-of select="current-grouping-key()"/>
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
