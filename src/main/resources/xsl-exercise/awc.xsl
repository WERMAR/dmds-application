<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet version="1.0"
                xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
                xmlns:fo="http://www.w3.org/1999/XSL/Format">

    <!-- Template for transforming IndividualSurvey XML into XSL-FO -->
    <xsl:template match="IndividualSurvey">
        <fo:root>
            <fo:layout-master-set>
                <fo:simple-page-master master-name="survey-page" page-width="8.5in" page-height="11in">
                    <fo:region-body margin="1in"/>
                </fo:simple-page-master>
            </fo:layout-master-set>
            <fo:page-sequence master-reference="survey-page">
                <fo:flow flow-name="xsl-region-body">
                    <!-- Header -->
                    <fo:block font-size="16pt" font-weight="bold" text-align="center">Individual Survey Report</fo:block>
                    <!-- Personal Information -->
                    <fo:block font-size="14pt" font-weight="bold" margin-top="20pt">Personal Information:</fo:block>
                    <fo:block>Total Purchase YTD: <xsl:value-of select="TotalPurchaseYTD"/></fo:block>
                    <fo:block>Date First Purchase: <xsl:value-of select="DateFirstPurchase"/></fo:block>
                    <fo:block>Birth Date: <xsl:value-of select="BirthDate"/></fo:block>
                    <fo:block>Marital Status: <xsl:value-of select="MaritalStatus"/></fo:block>
                    <fo:block>Yearly Income: <xsl:value-of select="YearlyIncome"/></fo:block>
                    <fo:block>Gender: <xsl:value-of select="Gender"/></fo:block>
                    <fo:block>Total Children: <xsl:value-of select="TotalChildren"/></fo:block>
                    <fo:block>Number of Children at Home: <xsl:value-of select="NumberChildrenAtHome"/></fo:block>
                    <!-- Demographics -->
                    <fo:block font-size="14pt" font-weight="bold" margin-top="20pt">Demographics:</fo:block>
                    <fo:block>Education: <xsl:value-of select="Education"/></fo:block>
                    <fo:block>Occupation: <xsl:value-of select="Occupation"/></fo:block>
                    <fo:block>Home Owner Flag: <xsl:value-of select="HomeOwnerFlag"/></fo:block>
                    <fo:block>Number of Cars Owned: <xsl:value-of select="NumberCarsOwned"/></fo:block>
                    <fo:block>Commute Distance: <xsl:value-of select="CommuteDistance"/></fo:block>
                </fo:flow>
            </fo:page-sequence>
        </fo:root>
    </xsl:template>
</xsl:stylesheet>
