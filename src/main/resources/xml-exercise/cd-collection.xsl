<?xml version="1.0" encoding="ISO-8859-1"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    <xsl:output method="html" encoding="ISO-8859-1"/>
    <xsl:template match="/collection">
        <html>
            <head>
                <title>CD Collection</title>
            </head>
            <body>
                <h3>Stats</h3>
                <p>Total of CDs:
                    <xsl:variable name="numberOfCds" select="count(//cd)"/>
                    <xsl:value-of select="$numberOfCds"/>
                </p>
                <p>Total of Songs:
                    <xsl:variable name="numberOfSongs" select="count(//song)"/>
                    <xsl:value-of select="$numberOfSongs"/>
                </p>
                <hr/>
                <h1>The Collection</h1>
                <xsl:for-each select="//cd">
                    <blockquote>
                        <span>Artist:
                            <xsl:value-of select="./artist"/>
                        </span>
                        <br />
                        <span>Year of Publish:
                            <xsl:value-of select="./@year"/>
                        </span>
                        <br />
                        <span>Title:
                            <xsl:value-of select="./title"/>
                        </span>
                        <br />
                        <ul style="list-style-type: none">
                            <xsl:for-each select="./songlist/song">
                                <li><xsl:value-of select="." /></li>
                            </xsl:for-each>
                        </ul>
                    </blockquote>
                </xsl:for-each>
            </body>
        </html>
    </xsl:template>
</xsl:stylesheet>