using AutoMapper;
using CodeVerse.BrzoDoLokacije.Models.Entity;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;

namespace CodeVerse.BrzoDoLokacije.Common.Mapper
{
    public class MappingProfile : Profile
    {
        public MappingProfile()
        {
            CreateMap<User, UserViewModel>();
            CreateMap<UserViewModel, User>();
            CreateMap<UserRegistrationViewModel, User>();
            CreateMap<PostViewModel, Post>();
            CreateMap<Post, PostViewModel>()
                .ForMember(dest => dest.Tags, m => m.MapFrom(src => this.GetTagsList(src.Tags)))
                .ForMember(dest => dest.LatitudeCenter, m => m.MapFrom(src => double.Parse(src.LatitudeCenter)))
                .ForMember(dest => dest.LongitudeCenter, m => m.MapFrom(src => double.Parse(src.LongitudeCenter)))
                .ForMember(dest => dest.NumberOfComments, m => m.MapFrom(src => src.Comments != null 
                                                                                ? src.Comments.Count : 0))
                .ForMember(dest => dest.AvgRating, m => m.MapFrom(src => src.Ratings != null && src.Ratings.Count > 0 ? src.Ratings
                                                                                .Select(x => x.Rate).Average() : 0))
                .ForMember(dest => dest.NumberOfRatings, m => m.MapFrom(src => src.Ratings != null && src.Ratings.Count > 0 ? src.Ratings
                                                                                .Select(x => x.Rate).Count() : 0));
            CreateMap<Resource, ResourceViewModel>();
            CreateMap<ResourceViewModel, Resource>();
            CreateMap<Comment, CommentViewModel>();
            CreateMap<CommentViewModel, Comment>();
        }

        private List<string> GetTagsList(string? tags)
        {
            if (string.IsNullOrEmpty(tags))
            {
                return new List<string>();
            }
            else
            {
                return tags.Split(Constants.Constants.TagSpliter)
                    .Select(str => str.Trim())
                    .Where(str => !string.IsNullOrEmpty(str))
                    .ToList();
            }
        }
    }
}