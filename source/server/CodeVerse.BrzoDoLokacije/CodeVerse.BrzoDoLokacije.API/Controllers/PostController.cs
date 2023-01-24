using CodeVerse.BrzoDoLokacije.BL.Interfaces;
using CodeVerse.BrzoDoLokacije.Common.ConfigProvider;
using CodeVerse.BrzoDoLokacije.Models.ViewModel;
using ImageMagick;
using Microsoft.AspNetCore.Authorization;
using Microsoft.AspNetCore.Http;
using Microsoft.AspNetCore.Mvc;
using Swashbuckle.AspNetCore.SwaggerGen;

namespace CodeVerse.BrzoDoLokacije.API.Controllers
{
    public class PostController : AuthorizeBaseController
    {
        private readonly IPostBL _postBL;

        public PostController(IPostBL postBL)
        {
            _postBL = postBL;
        }

        [HttpGet("{userId:long}")]
        public async Task<IActionResult> GetUserPosts([FromRoute] long userId, [FromQuery] int pageIndex = 0, int pageSize = 20)
        {
            var result = await _postBL.GetPostsByUserId(userId, pageIndex, pageSize);
            return Ok(result);
        }

        [HttpGet("AllCoordinates")]
        public async Task<IActionResult> GetAllCoordinates()
        {
            var result = await _postBL.GetAllCoordinates();
            return Ok(result);
        }

        [HttpGet]
        public async Task<IActionResult> GetPosts([FromQuery] GetPostsRequest request)
        {
            var result = await _postBL.GetPosts(request);
            return Ok(result);
        }

        [HttpGet("GetPost/{postId:long}")]
        public async Task<IActionResult> GetPost([FromRoute] long postId)
        {
            var result = await _postBL.GetPost(postId);
            return Ok(result);
        }

        [HttpDelete("{postId:long}")]
        public async Task<IActionResult> DeletePost([FromRoute] long postId)
        {
            var result = await _postBL.DeletePost(postId);
            return Ok(result);
        }

        [HttpPost("AddComment")]
        public async Task<IActionResult> AddComment([FromBody] AddCommentRequest request)
        {
            var result = await _postBL.AddComment(request);
            return Ok(result);
        }

        [HttpDelete("DeleteComment/{commentId:long}")]
        public async Task<IActionResult> DeleteComment([FromRoute] long commentId)
        {
            var result = await _postBL.DeleteComment(commentId);
            return Ok(result);
        }

        [HttpPost("UpdateComment")]
        public async Task<IActionResult> UpdateComment([FromBody] UpdateCommentRequest request)
        {
            var result = await _postBL.UpdateComment(request);
            return Ok(result);
        }

        [HttpPost("RatePost")]
        public async Task<IActionResult> RatePost([FromBody] RatePostRequest request)
        {
            var result = await _postBL.RatePost(request);
            return Ok(result);
        }

        [HttpDelete("RatePost/{postId:long}")]
        public async Task<IActionResult> DeleteRating([FromRoute] long postId)
        {
            var result = await _postBL.RemoveRateFromPost(postId);
            return Ok(result);
        }

        [HttpGet("GetUserPostsCoordinates/{userId:long}")]
        public async Task<IActionResult> GetUserPostsCoordinates([FromRoute] long userId)
        {
            var result = await _postBL.GetUserPostsCoordinates(userId);
            return Ok(result);
        }

        [HttpGet("GetPostsByCoordinates")]
        public async Task<IActionResult> GetPostsByCoordinates([FromQuery] double latitude, double longitude)
        {
            var result = await _postBL.GetPostsByCoordinates(latitude, longitude);
            return Ok(result);
        }

        [HttpPost("CreateNewPost")]
        public async Task<IActionResult> CreateNewPost([FromBody] AddPostRequest request)
        {
            var result = await _postBL.CreateNewPost(request);
            return Ok(result);
        }

        [HttpPost("UploadImages/{postId:long}")]
        public async Task<IActionResult> Uploads([FromRoute] long postId, IFormFileCollection files)
        {
            if (files.Count > 0)
            {
                Dictionary<string, byte[]> bytes = new Dictionary<string, byte[]>();

                for (int i = 0; i < files.Count; i++)
                {
                    if (!ConfigProvider.AllowedPictureExtensions.Contains(files[i].FileName.Split(".")[1].Trim().ToUpper()))
                    {
                        return Ok(new ActionResultResponse("Slika nije podrzanog formata."));
                    }
                    else if (files[i].Length > 0)
                    {
                        using (var stream = new MemoryStream())
                        {
                            files[i].CopyTo(stream);
                            var fileBytes = stream.ToArray();

                            using (MagickImage image = new MagickImage(fileBytes))
                            {
                                image.Quality = ConfigProvider.ImageQuality;
                                if (image.Width > ConfigProvider.ImageWidth)
                                {
                                    double widthRatio = (double)ConfigProvider.ImageWidth / (double)image.Width;
                                    int newHeight = Convert.ToInt32(widthRatio * image.Height);
                                    image.Resize(ConfigProvider.ImageWidth, newHeight);
                                }
                                var fileName = $"{i}{files[i].FileName.Split(".")[0]}.{ConfigProvider.DefaultPictureExtension}";
                                bytes.Add(fileName, image.ToByteArray(MagickFormat.Jpeg));
                            }
                        }
                    }
                }
                
                var result = await _postBL.AddImagesForPost(postId, bytes);

                return Ok(result);
            }
            else
            {
                return Ok(new ActionResultResponse("Niste poslali fajlove."));
            }
        }
    }
}