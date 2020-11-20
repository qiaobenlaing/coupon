//
//  BMSQ_CommitViewController.m
//  BMSQC
//
//  Created by liuqin on 16/3/14.
//  Copyright © 2016年 djx. All rights reserved.
//

#import "BMSQ_CommitViewController.h"
#import "BMSQ_startView.h"

#import "ZYQAssetPickerController.h"
#import "BMSQ_imageView.h"
#import "CWStarRateView.h"
#import "MobClick.h"
@interface BMSQ_CommitViewController ()<UIActionSheetDelegate,UINavigationControllerDelegate,ZYQAssetPickerControllerDelegate,UIImagePickerControllerDelegate,BMSQ_imageViewDelegate,UITextViewDelegate>

@property (nonatomic, strong)UITextView *textView;
@property (nonatomic, strong)UIScrollView *scImage;
@property (nonatomic, strong)NSMutableArray *seleImage;


@property (nonatomic,strong)NSString *wholeLvl;     //总体星级
@property (nonatomic,strong)NSString *effectLvl;    //效果星级
@property (nonatomic,strong)NSString *teacherLvl;   //师资星级
@property (nonatomic,strong)NSString *envLvl;       //环境资星级

@property (nonatomic, strong)NSMutableString *imageUrlS;
@end


@implementation BMSQ_CommitViewController

- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"CommitViewController"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"CommitViewController"];
}


-(void)viewDidLoad{
    
    [super viewDidLoad];
    
    [self setNavTitle:@"评论"];
    [self setNavigationBar];
    [self setNavBackItem];
    
    self.wholeLvl = @"0";
    self.effectLvl = @"0";
    self.teacherLvl = @"0";
    self.envLvl = @"0";
    self.imageUrlS = [[NSMutableString alloc]init];
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(startView:) name:@"starRateView" object:nil];
    
    self.seleImage = [[NSMutableArray alloc]init];
    
    UIButton *righBtn = [[UIButton alloc]initWithFrame:CGRectMake(APP_VIEW_WIDTH-50, 20, 50, 44)];
    [righBtn setTitle:@"发表" forState:UIControlStateNormal];
    righBtn.titleLabel.font = [UIFont boldSystemFontOfSize:12];
    [righBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [righBtn addTarget:self action:@selector(clickrightButton) forControlEvents:UIControlEventTouchUpInside];
    [self setNavRightBarItem:righBtn];
    
    BMSQ_startView *starView = [[BMSQ_startView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 30*4+20 +10)];
    [self.view addSubview:starView];
    
   self.textView = [[UITextView alloc]initWithFrame:CGRectMake(0, starView.frame.origin.y+starView.frame.size.height+1, APP_VIEW_WIDTH, 120)];
    self.textView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.textView];
    
    self.scImage = [[UIScrollView alloc]initWithFrame:CGRectMake(0, self.textView.frame.origin.y+self.textView.frame.size.height+10, APP_VIEW_WIDTH, 200)];
    self.scImage.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.scImage];
    
    UIButton *cameraBtn = [[UIButton alloc]initWithFrame:CGRectMake(0, 0, 100, 100)];
    [cameraBtn setImage:[UIImage imageNamed:@"camera"] forState:UIControlStateNormal];
//    cameraBtn.backgroundColor = [UIColor redColor];
    [cameraBtn addTarget:self action:@selector(cilckCamera) forControlEvents:UIControlEventTouchUpInside];
    
    [self.scImage addSubview:cameraBtn];

}
-(void)startView:(NSNotification *)notification{

    
    NSArray *array = notification.object;
    NSString *oneObject = [array objectAtIndex:0];
    NSString *lvl = [array objectAtIndex:1];

    if ([oneObject isEqualToString:@"100"]) {
        self.wholeLvl = lvl;
    }else if ([oneObject isEqualToString:@"200"]){
        self.effectLvl = lvl;
    }else if ([oneObject isEqualToString:@"300"]){
        self.teacherLvl = lvl;
    }else if ([oneObject isEqualToString:@"400"]){
        self.envLvl = lvl;
    }
    
}
-(void)cilckCamera{
    
    UIActionSheet *sheet = [[UIActionSheet alloc] initWithTitle:nil delegate:self cancelButtonTitle:@"取消" destructiveButtonTitle:nil otherButtonTitles:@"相机选择",@"拍照", nil];
    sheet.tag = 101;
    [sheet showInView:self.view];

}

- (void)actionSheet:(UIActionSheet *)actionSheet clickedButtonAtIndex:(NSInteger)buttonIndex
{
  if(actionSheet.tag == 101)
    {
        if (buttonIndex == 1) { //相机
            UIImagePickerControllerSourceType sourceType = UIImagePickerControllerSourceTypeCamera;
            if (![UIImagePickerController isSourceTypeAvailable: UIImagePickerControllerSourceTypeCamera]) {
                sourceType = UIImagePickerControllerSourceTypePhotoLibrary;
            }
            UIImagePickerController *picker = [[UIImagePickerController alloc] init];
            picker.delegate = self;
            picker.allowsEditing = YES;
            picker.sourceType = sourceType;
            [self presentViewController:picker animated:YES completion:nil];
            
        }else if (buttonIndex == 0) {  //相册
            ZYQAssetPickerController *picker = [[ZYQAssetPickerController alloc]init];
            
            picker.maximumNumberOfSelection = 6;
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
-(void)addPhotoImageS{
    

    for (UIView *va in self.scImage.subviews) {
        if([va isKindOfClass:[BMSQ_imageView class]]){
            [va removeFromSuperview];
        }
    }
    
    
    float w = 100;
    float spx = (APP_VIEW_WIDTH-300)/2;

    
    if (self.seleImage.count<=2) {
        for(int i=0;i<self.seleImage.count;i++){
            
            BMSQ_imageView *imageV = [[BMSQ_imageView alloc]initWithFrame:CGRectMake((i+1)*100+(i+1)*spx, 0, 100, 100)];
            [imageV.BgimageView setImage:[self.seleImage objectAtIndex:i]];
            imageV.delebutton.tag = 100+i;
            imageV.delegate = self;
            [self.scImage addSubview:imageV];
        }
    }else{
        
        NSArray *array = [self.seleImage subarrayWithRange:NSMakeRange(0, 2)];
        
        for(int i=0;i<array.count;i++){
            
            BMSQ_imageView *imageV = [[BMSQ_imageView alloc]initWithFrame:CGRectMake((i+1)*100+(i+1)*spx, 0, 100, 100)];
            [imageV.BgimageView setImage:[array objectAtIndex:i]];
            imageV.delebutton.tag = 100+i;
            imageV.delegate = self;
            [self.scImage addSubview:imageV];
        }
        
        for(int i=0;i<self.seleImage.count-2;i++){
            
            int x = i%3;
            int y = i/3;
            
            BMSQ_imageView *imageV = [[BMSQ_imageView alloc]initWithFrame:CGRectMake((100+spx)*x,(100+spx)+y*(100+spx), 100, 100)];
            [imageV.BgimageView setImage:[self.seleImage objectAtIndex:i+2]];
            imageV.delebutton.tag = 100+i+2;
            imageV.delegate = self;
            [self.scImage addSubview:imageV];
        }
        int i =( self.seleImage.count-2)%3==0?(int)self.seleImage.count/3:(int)self.seleImage.count/3+1;
        self.scImage.contentSize = CGSizeMake(APP_VIEW_WIDTH, i*100+i*spx);
    }
  

    
}



- (void)postRequestWithURLString:(NSString *)urlString
                       imageData:(NSData *)imageData
                      parameters:(NSDictionary *)postParams
                        fileName:(NSString *)picFileName
                       completed:(void(^)(BOOL success,NSString *errMsg))finished
{
    if (!imageData) {
        return;
    }
    //AF上传
    AFHTTPRequestOperationManager *manager = [AFHTTPRequestOperationManager manager];
    manager.responseSerializer = [AFHTTPResponseSerializer serializer];
    
    AFHTTPRequestOperation *operation = [manager POST:urlString parameters:postParams constructingBodyWithBlock:^(id<AFMultipartFormData> formData) {
        [formData appendPartWithFileData:imageData name:@"file" fileName:picFileName mimeType:@"image/jpeg"];
    } success:^(AFHTTPRequestOperation *operation, id responseObject) {
        //上传成功
        NSDictionary *dict = [NSJSONSerialization JSONObjectWithData:responseObject options:NSJSONReadingMutableContainers error:nil];
        [self.imageUrlS appendString:[NSString stringWithFormat:@"%@|",[dict objectForKey:@"code"]]];
        if (finished) {
            finished(YES,nil);
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        //上传失败
        NSLog(@"上传失败");
        if (finished) {
            finished(NO,error.description);
        }
    }];
}

- (void)sendImage{
    [SVProgressHUD showWithStatus:@""];
    
    [self uploadWithArray:self.seleImage uploadIndex:0];
    
}

- (void)uploadWithArray:(NSArray *)imageList uploadIndex:(NSInteger )index{
    
    // 上传的参数名
    NSString * Name = [NSString stringWithFormat:@"%@%zi", [NSString stringWithFormat:@"%@Act",[gloabFunction getShopCode]], 1];
    // 上传filename
    NSString * fileName = [NSString stringWithFormat:@"%@.jpg", Name];
    
    
    UIImage *img=[imageList objectAtIndex:index];
    NSData *imageData = UIImageJPEGRepresentation(img,1.0);
    [self postRequestWithURLString:[NSString stringWithFormat:@"%@/uploadImg",APP_SERVERCE_COMM_URL] imageData:imageData parameters:nil fileName:fileName completed:^(BOOL success, NSString *errMsg) {
        
        
        if (success) {
            NSLog(@"第%d个图片，上传成功！",(int)index);
        }else{
            NSLog(@"第%d个图片，上传失败！",(int)index);
        }
        
        if (index+1<[imageList count]) {
            [self uploadWithArray:imageList uploadIndex:index+1];
        }else{
            
            [self initJsonPrcClient:@"2"];
            NSMutableDictionary* paramsDic = [[NSMutableDictionary alloc]init];
            
            [paramsDic setObject:[gloabFunction getUserCode] forKey:@"userCode"];
            
            NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
            NSString *longitude = [userDefults objectForKey:LONGITUDE];
            NSString *latitude  = [userDefults objectForKey:LATITUDE];
            NSString *cityName = [userDefults objectForKey:SELECITY];
            [paramsDic setObject:longitude forKey:@"longitude"];
            [paramsDic setObject:latitude forKey:@"latitude"];
            [paramsDic setObject:cityName forKey:@"city"];
            
            if (self.imageUrlS.length>0) {
                [self.imageUrlS deleteCharactersInRange:NSMakeRange(self.imageUrlS.length-1, 1)];
            }
    
            [self addClassRemark];
            
            
        }
        
    }];
}



-(void)clickrightButton{
    
    
    if (self.seleImage.count>0) {
        [self sendImage];
    }else{
        [self addClassRemark];
    }
    
}
-(void)addClassRemark{
    
    [SVProgressHUD showWithStatus:@""];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    
    
    [params setObject:[gloabFunction getUserCode] forKey:@"userCode"];
    [params setObject:self.classCode forKey:@"classCode"];
    [params setObject:self.effectLvl forKey:@"effectLvl"];
    [params setObject:self.envLvl forKey:@"envLvl"];
    [params setObject:self.teacherLvl forKey:@"teacherLvl"];
    [params setObject:self.wholeLvl forKey:@"wholeLvl"];
    [params setObject:self.imageUrlS forKey:@"remarkImg"];
    [params setObject:@"18667115776" forKey:@"remark"];
    
    
    NSString* vcode = [gloabFunction getSign:@"addClassRemark" strParams:self.classCode];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    
    
    [self.jsonPrcClient invokeMethod:@"addClassRemark" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        NSString *code = [NSString stringWithFormat:@"%@",[responseObject objectForKey:@"code"]];
        if ([code intValue] ==50000) {
            [SVProgressHUD showSuccessWithStatus:@"评论成功"];
        }else if([code intValue] ==78000){
            [SVProgressHUD showErrorWithStatus:@"没有权限评论"];
            
        }else{
            [SVProgressHUD showErrorWithStatus:@"评论失败"];
            
        }
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD dismiss];
        
        CSAlert(@"修改失败");
    }];

}
@end
