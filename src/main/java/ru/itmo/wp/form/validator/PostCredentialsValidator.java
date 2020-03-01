package ru.itmo.wp.form.validator;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import ru.itmo.wp.form.PostCredentials;

import java.util.StringTokenizer;

@Component
public class PostCredentialsValidator implements Validator {
    @Override
    public boolean supports(Class<?> clazz) {
        return PostCredentials.class.equals(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        if (!errors.hasErrors()) {
            PostCredentials newPostForm = (PostCredentials) target;
            String inlineTags = newPostForm.getInlineTags();
            if (inlineTags != null) {
                StringTokenizer st = new StringTokenizer(inlineTags);
                while (st.hasMoreTokens()) {
                    if (!st.nextToken().matches("[a-z]{1,32}")) {
                        errors.rejectValue("inlineTags", "inlineTags.invalid-format", "invalid format of tags line");
                    }
                }
            }
        }
    }
}
