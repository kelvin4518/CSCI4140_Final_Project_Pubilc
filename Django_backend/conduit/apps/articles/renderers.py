from conduit.apps.core.renderers import ConduitJSONRenderer


class ArticleJSONRenderer(ConduitJSONRenderer):
    object_label = 'habit'
    pagination_object_label = 'habits'
    pagination_count_label = 'habitsCount'


class CommentJSONRenderer(ConduitJSONRenderer):
    object_label = 'check'
    pagination_object_label = 'checks'
    pagination_count_label = 'checksCount'

class BlogJSONRenderer(ConduitJSONRenderer):
    object_label = 'blog'
    pagination_object_label = 'blogs'
    pagination_count_label = 'blogsCount'
    
class ProfileJSONRenderer(ConduitJSONRenderer):
    object_label = 'profile'
    pagination_object_label = 'profiles'
    pagination_count_label = 'profilesCount'

