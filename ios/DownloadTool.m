//
//  DownloadTool.m
//  HotUpdateTest
//
//  Created by 王泽 on 2017/12/13.
//  Copyright © 2017年 Facebook. All rights reserved.
//

#import "DownloadTool.h"
#import "SSZipArchive.h"
#import "AFURLSessionManager.h"
#import "UpdateDataLoader.h"

@implementation DownLoadTool
+ (DownLoadTool *) defaultDownLoadTool{
  static DownLoadTool *sharedInstance = nil;
  static dispatch_once_t onceToken;
  
  dispatch_once(&onceToken, ^{
    sharedInstance = [[DownLoadTool alloc] init];
  });
  return sharedInstance;
}

//下载了一个dmg文件

-(void)downLoadWithUrl:(NSString*)url{
  //根据url下载相关文件
  NSURLSessionConfiguration *configuration = [NSURLSessionConfiguration defaultSessionConfiguration];
  AFURLSessionManager *manager = [[AFURLSessionManager alloc] initWithSessionConfiguration:configuration];
  NSURL *URL = [NSURL URLWithString:url];
  NSURLRequest *request = [NSURLRequest requestWithURL:URL];
  NSURLSessionDownloadTask *downloadTask = [manager downloadTaskWithRequest:request progress:^(NSProgress * _Nonnull downloadProgress) {
    //获取下载进度
    NSLog(@"Progress is %f", downloadProgress.fractionCompleted);
  } destination:^NSURL *(NSURL *targetPath, NSURLResponse *response) {
    //有返回值的block，返回文件存储路径
    NSURL *documentsDirectoryURL = [[NSFileManager defaultManager] URLForDirectory:NSDocumentDirectory inDomain:NSUserDomainMask appropriateForURL:nil create:NO error:nil];
    return [documentsDirectoryURL URLByAppendingPathComponent:@"bundle.zip"];
  } completionHandler:^(NSURLResponse *response, NSURL *filePath, NSError *error) {
    if(error){
      //下载出现错误
      NSLog(@"download error%@",error);
    }else{
      // [self showPromptWithStr:@"更新完毕。请重新启动******！"];
      //下载成功
      NSLog(@"File downloaded to: %@", filePath);
      self.zipPath = [[filePath absoluteString] substringFromIndex:7];
      //下载成功后更新本地存储信息
      NSDictionary*infoDic=@{@"bundleVersion":@3,@"downloadUrl":url};
      [UpdateDataLoader sharedInstance].versionInfo=infoDic;
      
      [[UpdateDataLoader sharedInstance] writeAppVersionInfoWithDictiony:[UpdateDataLoader sharedInstance].versionInfo];
      //解压并删除压缩包
      if ([self unZip]) {
        [self deleteZip];
      };
      
    }
  }];
  [downloadTask resume];
}

//解压压缩包
-(BOOL)unZip{
  if (self.zipPath == nil) {
    return NO;
  }
  //检查Document里有没有bundle文件夹
  NSString* bundlePath = [[UpdateDataLoader sharedInstance] iOSFileBundlePath];
  BOOL isDir;
  //如果有，则删除后解压，如果没有则直接解压
  if ([[NSFileManager defaultManager] fileExistsAtPath:bundlePath isDirectory:&isDir]&&isDir) {
    [[NSFileManager defaultManager] removeItemAtPath:bundlePath error:nil];
  }
  NSString *zipPath = self.zipPath;
  NSString *destinationPath = [[NSSearchPathForDirectoriesInDomains(NSDocumentDirectory, NSUserDomainMask, YES) objectAtIndex:0]stringByAppendingString:@"/IOSBundle/"];
  BOOL success = [SSZipArchive unzipFileAtPath:zipPath toDestination:destinationPath];
  NSLog(@"unzip is success: %@" ,success?@"YES":@"NO");
  return success;
}

//删除压缩包
-(void)deleteZip{
  NSError* error = nil;
  [[NSFileManager defaultManager] removeItemAtPath:self.zipPath error:&error];
}

@end
