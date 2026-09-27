package com.mallowigi.permify.settings

import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighter
import com.intellij.openapi.options.colors.AttributesDescriptor
import com.intellij.openapi.options.colors.ColorDescriptor
import com.intellij.openapi.options.colors.ColorSettingsPage
import com.intellij.openapi.util.NlsContexts
import com.mallowigi.permify.highlighter.PermifyHighlighterFactory
import org.jetbrains.annotations.NonNls
import javax.swing.Icon

class PermifyColorSettings : ColorSettingsPage {
  override fun getIcon(): Icon? = null

  override fun getHighlighter(): SyntaxHighlighter = PermifyHighlighterFactory().getSyntaxHighlighter(null, null)

  override fun getDemoText(): @NonNls String =
    """<entity>entity</entity> <entity_name>user</entity_name> <operator>{</operator><operator>}</operator>
<entity>entity</entity> <entity_name>bridge_user</entity_name> <operator>{</operator><operator>}</operator>

<comment>// region foo</comment>
<entity>entity</entity> <entity_name>bridge</entity_name> <operator>{</operator>
  <relation>relation</relation> <relation_name>customer_viewer</relation_name> <instance>@bridge_user</instance>
  <relation>relation</relation> <relation_name>customer_limited_viewer</relation_name> <instance>@bridge_user</instance>
  <relation>relation</relation> <relation_name>administrator</relation_name> <instance>@bridge_user</instance>
  <relation>relation</relation> <relation_name>permissions_manager</relation_name> <instance>@bridge_user</instance>
  <relation>relation</relation> <relation_name>super_admin</relation_name> <instance>@bridge_user</instance>

  <action>action</action> <action_name>edit_account_list</action_name> <operator>=</operator> <ident>super_admin</ident> <control>or</control> <ident>permissions_manager</ident>
  <action>action</action> <action_name>assign_permissions_manager</action_name> <operator>=</operator> <ident>super_admin</ident>
<operator>}</operator>
<comment>// endregion</comment>


<entity>entity</entity> <entity_name>customer_publisher</entity_name> <operator>{</operator>
  <relation>relation</relation> <relation_name>member</relation_name> <instance>@bridge_user</instance>
<operator>}</operator>

<entity>entity</entity> <entity_name>publisher</entity_name> <operator>{</operator>
  <relation>relation</relation> <relation_name>bridge</relation_name> <instance>@bridge</instance>
  <relation>relation</relation> <relation_name>accessor</relation_name> <instance>@bridge_user</instance>
  <relation>relation</relation> <relation_name>type</relation_name> <instance>@test_publisher</instance> <instance>@customer_publisher</instance>

  <permission>permission</permission> <permission_name>bridge_customer_limited_viewer</permission_name> <operator>=</operator> <ident>bridge</ident><dot>.</dot><ident>customer_limited_viewer</ident>
  <permission>permission</permission> <permission_name>bridge_customer_viewer</permission_name> <operator>=</operator> <ident>bridge</ident><dot>.</dot><ident>customer_viewer</ident>
  <permission>permission</permission> <permission_name>bridge_permissions_manager</permission_name> <operator>=</operator> <ident>bridge</ident><dot>.</dot><ident>permissions_manager</ident>
  <permission>permission</permission> <permission_name>bridge_admin</permission_name> <operator>=</operator> <ident>bridge</ident><dot>.</dot><ident>administrator</ident>
  <permission>permission</permission> <permission_name>bridge_super_admin</permission_name> <operator>=</operator> <ident>bridge</ident><dot>.</dot><ident>super_admin</ident>
  <permission>permission</permission> <permission_name>bridge_right_type</permission_name> <operator>=</operator> <operator>(</operator><ident>bridge_customer_viewer</ident> <control>and</control> <ident>type</ident><dot>.</dot><ident>member</ident><operator>)</operator> <control>or</control> <operator>(</operator><ident>bridge_customer_limited_viewer</ident> <control>and</control> <ident>type</ident><dot>.</dot><ident>member</ident><operator>)</operator>

  <permission>permission</permission> <permission_name>viewer_access <operator>=</operator> <ident>bridge_customer_viewer</ident> <control>or</control> <operator>(</operator><ident>bridge_customer_limited_viewer</ident> <control>and</control> <ident>accessor</ident><operator>)</operator>
  <permission>permission</permission> <permission_name>admin_access</permission_name> <operator>=</operator> <ident>bridge_permissions_manager</ident> <control>or</control> <ident>bridge_super_admin</ident> <control>or</control> <ident>bridge_admin</ident> <control>or</control> <ident>bridge_right_type</ident>

  <action>action</action> <action_name>deactivate</action_name> <operator>=</operator> <ident>admin_access</ident>
  <action>action</action> <action_name>access</action_name> <operator>=</operator> <ident>admin_access</ident> <control>or</control> <ident>viewer_access</ident>
  <action>action</action> <action_name>ff_edit</action_name> <operator>=</operator> <ident>admin_access</ident> <control>or</control> <ident>viewer_access</ident>
  <action>action</action> <action_name>add_feed_properties</action_name> <operator>=</operator> <ident>admin_access</ident> <control>or</control> <ident>viewer_access</ident>
<operator>}</operator>


<entity>entity</entity> <entity_name>test_section</entity_name> <operator>{</operator>
  <relation>relation</relation> <relation_name>sales</relation_name> <instance>@bridge_user</instance>
  <relation>relation</relation> <relation_name>viewer</relation_name> <instance>@bridge_user</instance>
  <relation>relation</relation> <relation_name>administrator</relation_name> <instance>@bridge_user</instance>

  <action>action</action> <action_name>admin</action_name> <operator>=</operator> <ident>administrator</ident> <control>or</control> <ident>sales</ident>
  <action>action</action> <action_name>view</action_name> <operator>=</operator> <ident>admin</ident> <control>or</control> <ident>viewer</ident>
<operator>}</operator>

<entity>entity</entity> <entity_name>predefined_role</entity_name> <operator>{</operator>
  <relation>relation</relation> <relation_name>member</relation_name> <instance>@user</instance>
<operator>}</operator>

<entity>entity</entity> <entity_name>section</entity_name> <operator>{</operator>
  <relation>relation</relation> <relation_name>administrator</relation_name> <instance>@user</instance> <instance>@predefined_role</instance><transitive>#member</transitive>
  <relation>relation</relation> <relation_name>editor</relation_name> <instance>@user</instance> <instance>@predefined_role</instance><transitive>#member</transitive>
  <relation>relation</relation> <relation_name>viewer</relation_name> <instance>@user</instance> <instance>@predefined_role</instance><transitive>#member</transitive>
  <relation>relation</relation> <relation_name>no_access</relation_name> <instance>@user</instance> <instance>@predefined_role</instance><transitive>#member</transitive>

  <relation>relation</relation> <relation_name>type</relation_name> <instance>@test_section</instance> <instance>@customer_section</instance>

  <action>action</action> <action_name>admin</action_name> <operator>=</operator> <ident>administrator</ident> <control>or</control> <ident>type</ident><dot>.</dot><extension>admin</extension>
  <action>action</action> <action_name>edit</action_name> <operator>=</operator> <ident>admin</ident> <control>or</control> <ident>editor</ident>
  <action>action</action> <action_name>view</action_name> <operator>=</operator> <ident>edit</ident> <control>or</control> <ident>viewer</ident> <control>or</control> <ident>type</ident><dot>.</dot><extension>view</extension>
<operator>}</operator>

<permission>permission</permission> <permission_name>read</permission_name> <operator>=</operator>  <ident>org</ident><dot>.</dot><extension>admin</extension> <control>and</control> <operator>(</operator><ident>owner</ident> <control>or</control> <ident>maintainer</ident> <control>or</control> <ident>org</ident><dot>.</dot><extension>member</extension><operator>)</operator>

<entity>entity</entity> <entity_name>organization</entity_name> <operator>{</operator>

<relation>relation</relation> <relation_name>admin</relation_name> <instance>@user</instance>

<attribute>attribute</attribute> <attribute_name>ip_range</attribute_name> <type>string[]</type>

<permission>permission</permission> <permission_name>view</permission_name> <operator>=</operator> <function>check_ip_range</function><operator>(</operator><ident>request</ident><dot>.</dot><extension>ip</extension><operator>,</operator> <ident>ip_range</ident><operator>)</operator> <control>or</control> <ident>admin</ident>
<operator>}</operator>

<rule>rule</rule> <rule_name>check_ip_range</rule_name><operator>(</operator><param>ip</param> <type>string</type><operator>,</operator> <param>ip_range</param> <type>string[]</type><operator>)</operator> <operator>{</operator>
<ident>ip</ident> <control>in</control> <ident>ip_range</ident>
<operator>}</operator>

<entity>entity</entity> <entity_name>post</entity_name> <operator>{</operator>
<attribute>attribute</attribute> <attribute_name>is_public</attribute_name> <type>boolean</type>

<permission>permission</permission> <permission_name>view</permission_name> <operator>=</operator> <ident>is_public</ident>
<operator>}</operator>

<rule>rule</rule> <rule_name>check_location</rule_name><operator>(</operator><param>current_location</param> <type>string</type><operator>,</operator> <param>location</param> <type>string[]</type><operator>)</operator> <operator>{</operator>
<ident>current_location</ident> <control>in</control> <ident>location</ident>
<operator>}</operator>

<rule>rule</rule> <rule_name>check_age</rule_name><operator>(</operator><param>age</param> <type>integer</type><operator>)</operator> <operator>{</operator>
<ident>age</ident> <operator>>=</operator> <number>18</number> <control>&&</control> <ident>age</ident> <operator>></operator> <number>10</number>
<operator>}</operator>

<rule>rule</rule> <rule_name>is_weekday</rule_name><operator>(</operator><param>day_of_week</param> <type>string</type><operator>)</operator> <operator>{</operator>
  <ident>day_of_week</ident> <operator>!=</operator> <string>'saturday'</string> <control>&&</control> <ident>day_of_week</ident> <operator>!=</operator> <string>'sunday'</string>
<operator>}</operator>
""".trimIndent()

  override fun getAdditionalHighlightingTagToDescriptorMap(): Map<String, TextAttributesKey>? = DESCRIPTORS

  override fun getAttributeDescriptors(): Array<out AttributesDescriptor?> = ATTRIBUTES

  override fun getColorDescriptors(): Array<out ColorDescriptor?> = ColorDescriptor.EMPTY_ARRAY

  override fun getDisplayName(): @NlsContexts.ConfigurableName String = "Permify"

  companion object {
    private val DESCRIPTORS = mapOf(
      "entity" to PERMIFY_ENTITY,
      "entity_name" to PERMIFY_ENTITY_NAME,
      "relation" to PERMIFY_RELATION,
      "relation_name" to PERMIFY_RELATION_NAME,
      "action" to PERMIFY_ACTION,
      "action_name" to PERMIFY_ACTION_NAME,
      "permission" to PERMIFY_PERMISSION,
      "permission_name" to PERMIFY_PERMISSION_NAME,
      "attribute" to PERMIFY_ATTRIBUTE_KEYWORD,
      "attribute_name" to PERMIFY_ATTRIBUTE_NAME,
      "rule" to PERMIFY_RULE,
      "rule_name" to PERMIFY_RULE_NAME,
      "ident" to PERMIFY_IDENTIFIER,
      "dot" to PERMIFY_DOT,
      "operator" to PERMIFY_OPERATOR,
      "param" to PERMIFY_PARAMETER,
      "type" to PERMIFY_TYPE,
      "number" to PERMIFY_NUMBER,
      "string" to PERMIFY_STRING,
      "comment" to PERMIFY_COMMENT,
      "control" to PERMIFY_KEYWORD,
      "instance" to PERMIFY_REFERENCE,
      "function" to PERMIFY_FUNCTION,
      "keyword" to PERMIFY_KEYWORD,
      "extension" to PERMIFY_EXTENSION,
      "transitive" to PERMIFY_ATTRIBUTE
    )

    private val ATTRIBUTES: Array<AttributesDescriptor> = arrayOf(
      AttributesDescriptor("Entities//Entity", PERMIFY_ENTITY),
      AttributesDescriptor("Entities//Entity Name", PERMIFY_ENTITY_NAME),

      AttributesDescriptor("Relations//Relation", PERMIFY_RELATION),
      AttributesDescriptor("Relations//Relation Name", PERMIFY_RELATION_NAME),
      AttributesDescriptor("Relations//Related Entity", PERMIFY_REFERENCE),
      AttributesDescriptor("Relations//Transitive Relation", PERMIFY_ATTRIBUTE),

      AttributesDescriptor("Permissions//Permission", PERMIFY_PERMISSION),
      AttributesDescriptor("Permissions//Permission Name", PERMIFY_PERMISSION_NAME),
      AttributesDescriptor("Permissions//Dot", PERMIFY_DOT),
      AttributesDescriptor("Permissions//Nested Permission", PERMIFY_EXTENSION),

      AttributesDescriptor("Actions//Action", PERMIFY_ACTION),
      AttributesDescriptor("Actions//Action Name", PERMIFY_ACTION_NAME),

      AttributesDescriptor("Attributes//Attribute", PERMIFY_ATTRIBUTE_KEYWORD),
      AttributesDescriptor("Attributes//Attribute Name", PERMIFY_ATTRIBUTE_NAME),

      AttributesDescriptor("Rules//Rule", PERMIFY_RULE),
      AttributesDescriptor("Rules//Rule Name", PERMIFY_RULE_NAME),
      AttributesDescriptor("Rules//Parameter", PERMIFY_PARAMETER),
      AttributesDescriptor("Rules//Parameter Type", PERMIFY_TYPE),

      AttributesDescriptor("Common//Identifier", PERMIFY_IDENTIFIER),
      AttributesDescriptor("Common//Operator", PERMIFY_OPERATOR),
      AttributesDescriptor("Common//Number", PERMIFY_NUMBER),
      AttributesDescriptor("Common//String", PERMIFY_STRING),
      AttributesDescriptor("Common//Comment", PERMIFY_COMMENT),
      AttributesDescriptor("Common//Keyword", PERMIFY_KEYWORD),
    )
  }
}
