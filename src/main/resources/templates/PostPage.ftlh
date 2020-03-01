<#-- @ftlvariable name="post" type="ru.itmo.wp.domain.Post" -->
<#-- @ftlvariable name="newComment" type="ru.itmo.wp.domain.Comment" -->

<#import "/spring.ftl" as spring>
<#import "commons.ftlh" as c>

<@c.page>
    <@c.post post=post/>

    <#if post??>
        <#if user?has_content>
            <div class="form-box">
                <div class="header">Write Comment</div>
                <div class="body">
                    <form method="post" action="">
                        <input type="hidden" name="action" value="/post/${post.id}"/>
                        <div class="field">
                            <div class="name">
                                <label for="text">Text</label>
                            </div>
                            <div class="value">
                                <textarea class="text" id="text" name="text">${newComment.text!}</textarea>
                            </div>
                            <@c.error "newComment.text"/>
                        </div>
                        <div class="button-field">
                            <input type="submit" value="Post">
                        </div>
                    </form>
                </div>
            </div>
        </#if>

        <#list post.comments as viewedComment>
            <@c.comment comment=viewedComment/>
        </#list>
    <#else>
        No such post
    </#if>
</@c.page>