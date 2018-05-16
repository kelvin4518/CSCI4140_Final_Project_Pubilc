from django.db import models

from conduit.apps.core.models import TimestampedModel


class Article(TimestampedModel):
    slug = models.SlugField(db_index=True, max_length=255, unique=True)
    title = models.CharField(db_index=True, max_length=255)
    description = models.TextField()
    body = models.TextField()
    start_date = models.DateField(blank=True,default="2018-01-01")
    end_date = models.DateField(blank=True,default="2018-01-01")

    start_time = models.TimeField(blank=True,default="00:00")
    end_time = models.TimeField(blank=True,default="23:59")

    is_public = models.IntegerField(default=1)
    send_notification = models.IntegerField(default=1)
    
    # Every article must have an author. This will answer questions like "Who
    # gets credit for writing this article?" and "Who can edit this article?".
    # Unlike the `User` <-> `Profile` relationship, this is a simple foreign
    # key (or one-to-many) relationship. In this case, one `Profile` can have
    # many `Article`s.
    author = models.ForeignKey(
        'profiles.Profile', on_delete=models.CASCADE, related_name='articles'
    )

    members = models.ManyToManyField(
        'profiles.Profile',
        related_name='habits',
    )
    
    tags = models.ManyToManyField(
        'articles.Tag', related_name='articles'
    )

    def get_title(self):
        return self.title


class Comment(TimestampedModel):
    body = models.TextField(blank=True)
    score = models.IntegerField()

    article = models.ForeignKey(
        'articles.Article', related_name='comments', on_delete=models.CASCADE
    )

    author = models.ForeignKey(
        'profiles.Profile', related_name='comments', on_delete=models.CASCADE
    )
    
    class Meta:
        ordering=("-score",)


class Blog(TimestampedModel):
    blog = models.TextField()
    blog_img = models.FileField(blank=True, null=True)
    like = models.IntegerField(default=0)
    article = models.ForeignKey(
        'articles.Article', related_name='blogs', on_delete=models.CASCADE
    )

    author = models.ForeignKey(
        'profiles.Profile', related_name='blogs', on_delete=models.CASCADE
    )


class Tag(TimestampedModel):
    tag = models.CharField(max_length=255)
    slug = models.SlugField(db_index=True, unique=True)

    def __str__(self):
        return self.tag
