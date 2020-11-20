//
//  BMSQ_ActivitySettingViewController2ViewController.m
//  BMSQS
//
//  Created by liuqin on 15/10/28.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_ActivitySettingViewController2ViewController.h"
#import "ZYQAssetPickerController.h"
#import "BMSQ_imageView.h"

#import "SVProgressHUD.h"

@interface BMSQ_ActivitySettingViewController2ViewController ()<UIActionSheetDelegate, UINavigationControllerDelegate, UIImagePickerControllerDelegate,ZYQAssetPickerControllerDelegate,BMSQ_imageViewDelegate>


@property (nonatomic,strong)UIView *photoView;
@property (nonatomic, strong)UIImageView *fristImageView;

@property (nonatomic,strong)NSMutableArray *rectS;
@property (nonatomic,strong)NSMutableArray *seleImage;

@end

@implementation BMSQ_ActivitySettingViewController2ViewController

- (void)viewDidLoad {
    [super viewDidLoad];

    [self setNavTitle:@"营销活动设置"];
    [self setNavBackItem];

    self.view.backgroundColor = [UIColor whiteColor];
    self.rectS = [[NSMutableArray alloc]init];
    self.seleImage = [[NSMutableArray alloc]init];
    
    float w = (APP_VIEW_WIDTH-40)/3;

    self.photoView = [[UIView alloc]initWithFrame:CGRectMake(10, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH-20, (w+20)*2+20)];
    self.photoView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.photoView];
 
    
    
    for (int i = 0 ;i<3;i++) {
        NSValue *value = [NSValue valueWithCGRect:CGRectMake(i*(w+10), 10,  w,  w+10)];
        [self.rectS addObject:value];
    }
    
    for (int i = 0 ;i<3;i++) {
        NSValue *value = [NSValue valueWithCGRect:CGRectMake(i*(w+10),w+40,  w,  w+10)];
        [self.rectS addObject:value];
    }

    
    self.fristImageView = [[UIImageView alloc]initWithFrame:[[self.rectS objectAtIndex:0]CGRectValue]];
    [self.fristImageView setImage:[UIImage imageNamed:@"icon_addpic_unfocused"]];
    self.fristImageView.userInteractionEnabled = YES;
    [self.photoView addSubview:self.fristImageView];

    UITapGestureRecognizer *tap = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickButton)];
    [self.fristImageView addGestureRecognizer:tap];

    
    UIButton *beforeBtn = [[UIButton alloc]initWithFrame:CGRectMake(10, self.photoView.frame.origin.y+self.photoView.frame.size.height +10, (APP_VIEW_WIDTH-40)/2, 40)];
    [beforeBtn setTitle:@"上一步" forState:UIControlStateNormal];
    [beforeBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [beforeBtn addTarget:self action:@selector(clickBefore) forControlEvents:UIControlEventTouchUpInside];
    beforeBtn.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [self.view addSubview:beforeBtn];

    UIButton *nextBtn = [[UIButton alloc]initWithFrame:CGRectMake(20+(APP_VIEW_WIDTH-40)/2, beforeBtn.frame.origin.y, (APP_VIEW_WIDTH-40)/2, 40)];
    [nextBtn setTitle:@"下一步" forState:UIControlStateNormal];
    [nextBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    nextBtn.backgroundColor = UICOLOR(182, 0, 12, 1.0);
    [nextBtn addTarget:self action:@selector(clickNext) forControlEvents:UIControlEventTouchUpInside];
    [self.view addSubview:nextBtn];
    
    
}

-(void)clickBefore{
    [self.navigationController popViewControllerAnimated:YES];
}
-(void)clickNext{
      [SVProgressHUD showWithStatus:@"" maskType:SVProgressHUDMaskTypeClear];
    
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
     // 显示进度
    [manager POST:[NSString stringWithFormat:@"%@/uploadImg",APP_SERVERCE_COMM_URL] parameters:nil constructingBodyWithBlock:^(id<AFMultipartFormData> formData)
     {
         
         // 上传 多张图片
         for(NSInteger i = 0; i < self.seleImage.count; i++)
         {
             UIImage *image = [self.seleImage objectAtIndex: i];
             NSData *imageData = UIImageJPEGRepresentation(image,1.0);
             // 上传的参数名
             NSString * Name = [NSString stringWithFormat:@"%@%zi", [NSString stringWithFormat:@"%@Act",[gloabFunction getShopCode]], i+1];
             // 上传filename
             NSString * fileName = [NSString stringWithFormat:@"%@.jpg", Name];
             
             [formData appendPartWithFileData:imageData name:Name fileName:fileName mimeType:@"image/jpeg"];
         }
     }
          success:^(AFHTTPRequestOperation *operation, id responseObject)
     {
         
         NSString *result = [[NSString alloc] initWithData:responseObject encoding:NSUTF8StringEncoding];
         
         NSData *jsonData = [result dataUsingEncoding:NSUTF8StringEncoding];
         NSError *err;
         NSDictionary *dic = [NSJSONSerialization JSONObjectWithData:jsonData
                                                             options:NSJSONReadingMutableContainers
                                                               error:&err];
         
         [self addaddActivity:dic];
         
     }
          failure:^(AFHTTPRequestOperation *operation, NSError *error)
     {
         NSLog(@"错误 %@", error.localizedDescription);
     }];
    
    
}

-(void)addaddActivity:(NSDictionary *)dic{
    
    NSDateFormatter *dataFormatter = [[NSDateFormatter alloc] init];
    dataFormatter.dateFormat = @"YYYY-MM-DD hh:mm:ss";
    
    [self initJsonPrcClient:@"1"];
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params addEntriesFromDictionary:_dataDic];

    NSString* vcode = [gloabFunction getSign:@"addActivity" strParams:_dataDic[ @"activityName"]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    [params setObject:@"3" forKey:@"activityBelonging"];
    [params setObject:[gloabFunction getShopCode] forKey:@"shopCode"];
    [params setObject:[gloabFunction getStaffCode] forKey:@"creatorCode"];
    [params setObject:@"" forKey:@"activityLogo"];
    
    if ([dic objectForKey:@"code"] ) {
        [params setObject:[dic objectForKey:@"code"] forKey:@"activityImg"];

    }else{
        [params setObject:@"" forKey:@"activityImg"];
 
    }
    
    __weak __typeof(self)weakSelf = self;

    [self.jsonPrcClient invokeMethod:@"addActivity" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        [SVProgressHUD dismiss];
        int resCode = [[responseObject objectForKey:@"code"] intValue];
 
        switch (resCode) {
            case 5000:
            {
                CSAlert(@"添加成功");
                NSArray *vcs = weakSelf.navigationController.viewControllers;
                [weakSelf.navigationController popToViewController:[vcs objectAtIndex:1] animated:YES];
                [[NSNotificationCenter defaultCenter] postNotificationName:@"actRefresh" object:nil];
                
            }
                break;
            case 20000:
                CSAlert(@"失败，请重试");
                break;
            case 50200:
                CSAlert(@"活动主题不正确");
                break;
            case 50201:
                CSAlert(@"活动开始时间不正确");
                break;
            case 50202:
                CSAlert(@"活动结束时间不正确");
                break;
            case 50203:
                CSAlert(@"活动地点不正确");
                break;
            case 50204:
                CSAlert(@"活动说明不正确");
                break;
            case 50205:
                CSAlert(@"活动参与人数上限不正确");
                break;
            case 50206:
                CSAlert(@"是否需要预付费不正确");
                break;
            case 50207:
                CSAlert(@"预付金额不正确");
                break;
            case 50208:
                CSAlert(@"是否需要报名不正确");
                break;
            case 50209:
                CSAlert(@"活动图片不正确");
                break;
            case 50210:
                CSAlert(@"活动方形图片不正确");
                break;
            case 50314:
                CSAlert(@"商家编码不正确");
                break;
            case 50212:
                CSAlert(@"活动发起人编码不正确");
                break;
            case 50213:
                CSAlert(@"活动归属不正确");
                break;
            case 50214:
                CSAlert(@"活动开始时间不能大于活动");
                break;
            default:
                break;
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];

        NSLog(@"code");
    }];
    
    
    
}


-(void)addPhotoImageS{
    
    for (UIView *va in self.photoView.subviews) {
        if([va isKindOfClass:[BMSQ_imageView class]]){
            [va removeFromSuperview];
        }
    }
    
    for(int i=0;i<self.seleImage.count;i++){
        
        BMSQ_imageView *imageV = [[BMSQ_imageView alloc]initWithFrame:[[self.rectS objectAtIndex:i]CGRectValue]];
        [imageV.BgimageView setImage:[self.seleImage objectAtIndex:i]];
        imageV.delebutton.tag = 100+i;
        imageV.delegate = self;
        [self.photoView addSubview:imageV];
        
    }
    
    if (self.seleImage.count<6) {
        self.fristImageView.hidden = NO;
        self.fristImageView.frame = [[self.rectS objectAtIndex:self.seleImage.count]CGRectValue];
        if(self){
            
        }
    }else{
        self.fristImageView.hidden = YES;
    }
    
    
}

-(void)clickButton{
  
    UIActionSheet *actionSheet = [[UIActionSheet alloc]
                                  initWithTitle:nil
                                  delegate:self
                                  cancelButtonTitle:@"取消"
                                  destructiveButtonTitle:nil
                                  otherButtonTitles:@"相机", @"相册",nil];
    actionSheet.actionSheetStyle = UIActionSheetStyleBlackOpaque;
    [actionSheet showInView:self.view];
}
-(void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
    if (buttonIndex == 0) { //相机
        UIImagePickerControllerSourceType sourceType = UIImagePickerControllerSourceTypeCamera;
        if (![UIImagePickerController isSourceTypeAvailable: UIImagePickerControllerSourceTypeCamera]) {
            sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
        }
        UIImagePickerController *picker = [[UIImagePickerController alloc] init];
        picker.delegate = self;
        picker.allowsEditing = YES;
        picker.sourceType = sourceType;
        [self presentViewController:picker animated:YES completion:nil];
      
    }else if (buttonIndex == 1) {  //相册
        ZYQAssetPickerController *picker = [[ZYQAssetPickerController alloc]init];
        
        picker.maximumNumberOfSelection = 6 - self.seleImage.count;
        picker.assetsFilter = [ALAssetsFilter allPhotos];
        picker.showEmptyGroups = NO;
        picker.delegate = self;
        picker.selectionFilter = [NSPredicate predicateWithBlock:^BOOL(id evaluatedObject,NSDictionary *bindings){
            if ([[(ALAsset *)evaluatedObject valueForProperty:ALAssetPropertyType]isEqual:ALAssetTypeVideo]) {
                NSTimeInterval duration = [[(ALAsset *)evaluatedObject valueForProperty:ALAssetPropertyDuration]doubleValue];
                return duration >= 5;
            }else{
                return  YES;
            }
        }];
        [self presentViewController:picker animated:YES completion:nil];

        
    }else if(buttonIndex == 2) {  //取消
        
        
    }
    
}
- (void)saveImage:(UIImage *)image {
    NSLog(@"保存");
}
#pragma mark –
#pragma mark Camera View Delegate Methods
- (void)imagePickerController:(UIImagePickerController *)picker didFinishPickingMediaWithInfo:(NSDictionary *)info {
    [picker dismissViewControllerAnimated:YES completion:^(){
        UIImage *portraitImg = [info objectForKey:@"UIImagePickerControllerOriginalImage"];
        [self.seleImage addObject:portraitImg];
        [self addPhotoImageS];

    }];
}
- (void)imagePickerControllerDidCancel:(UIImagePickerController *)picker {
    [picker dismissViewControllerAnimated:YES completion:nil];
}
-(void)assetPickerController:(ZYQAssetPickerController *)picker didFinishPickingAssets:(NSArray *)assets{
    
    
    for ( int i=0;i<assets.count;i++) {
        ALAsset *asset = [assets objectAtIndex:i];
        UIImage *tempImg=[UIImage imageWithCGImage:asset.defaultRepresentation.fullScreenImage];
        [self.seleImage addObject:tempImg];
        
    }
    [self addPhotoImageS];
   
    [picker dismissViewControllerAnimated:YES completion:Nil];
    
}
-(void)deleActImage:(int)tag{
    [self.seleImage removeObjectAtIndex:tag-100];
    [self addPhotoImageS];
}

@end
