//
//  BMSQ_correctionViewController.m
//  BMSQC
//
//  Created by liuqin on 15/12/14.
//  Copyright © 2015年 djx. All rights reserved.
//

#import "BMSQ_correctionViewController.h"
#import "ZYQAssetPickerController.h"
#import "BMSQ_imageView.h"
#import "MobClick.h"

@interface BMSQ_correctionViewController ()<UIActionSheetDelegate,UINavigationControllerDelegate,ZYQAssetPickerControllerDelegate,UIImagePickerControllerDelegate,BMSQ_imageViewDelegate,UITextViewDelegate>

@property (nonatomic, strong)NSArray *resultDic;
@property (nonatomic, strong)UITableView *errTableView;

@property (nonatomic, strong)UITextField *errTextField;

@property (nonatomic,strong)NSMutableArray *seleImage;
@property (nonatomic,strong)UIView *secView;
@property (nonatomic,strong)NSMutableArray *rectS;

@property (nonatomic,strong)UIButton *fristImageView;
@property (nonatomic,strong)UITextView *textView;

@property (nonatomic, strong)NSString *errorcode;

@property (nonatomic,strong)NSMutableString *imageUrlS;


@property (nonatomic,strong)UIButton *submitBtn;

@property (nonatomic, assign)BOOL isSeleInfo;
@property (nonatomic, assign)BOOL isSeleContent;

@end


@implementation BMSQ_correctionViewController


- (void)viewWillAppear:(BOOL)animated
{
    [super viewWillAppear:animated];
    [MobClick beginLogPageView:@"correction"];// 
}
- (void)viewWillDisappear:(BOOL)animated
{
    [super viewWillDisappear:animated];
    [MobClick endLogPageView:@"correction"];
}


-(void)viewDidLoad{
    [super viewDidLoad];
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:@"纠错"];
    
    
    
    [[NSNotificationCenter defaultCenter]addObserver:self selector:@selector(enableNext) name:@"enableNext" object:nil];
    
    self.seleImage = [[NSMutableArray alloc]init];
    self.imageUrlS = [[NSMutableString alloc]init];
    UIView *topView =[[UIView alloc]initWithFrame:CGRectMake(0, APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 45)];
    topView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:topView];
    
    UILabel *titleLable = [[UILabel alloc]initWithFrame:CGRectMake(10, 0,60, 45)];
    titleLable.text = @"错误信息";
    titleLable.font = [UIFont systemFontOfSize:14];
    titleLable.textColor = APP_TEXTCOLOR;
    titleLable.textAlignment = NSTextAlignmentCenter;
    [topView addSubview:titleLable];
    
    UIView *buttonV = [[UIView alloc]initWithFrame:CGRectMake(90, 10,120, 25)];
    buttonV.backgroundColor = [UIColor clearColor];
    buttonV.layer.borderColor = [UICOLOR(226, 226, 226, 1) CGColor];
    buttonV.layer.borderWidth = 0.6;
    [topView addSubview:buttonV];
    
    self.errTextField = [[UITextField alloc]initWithFrame:CGRectMake(2, 0, 100, 25)];
    self.errTextField.placeholder = @"选择错误";
    self.errTextField.textColor = APP_TEXTCOLOR;
    self.errTextField.font = [UIFont systemFontOfSize:12];
    self.errTextField.enabled = NO;
    [buttonV addSubview:self.errTextField];
    
    UIImageView *imageView = [[UIImageView alloc]initWithFrame:CGRectMake(buttonV.frame.size.width-10, 10, 6, 3)];
    [imageView setImage:[UIImage imageNamed:@"down_gray"]];
    [buttonV addSubview:imageView];
    
    buttonV.userInteractionEnabled = YES;
    UITapGestureRecognizer *tapGesture = [[UITapGestureRecognizer alloc]initWithTarget:self action:@selector(clickErr)];
    [buttonV addGestureRecognizer:tapGesture];
    
    
    self.secView = [[UIView alloc]initWithFrame:CGRectMake(0, 55+APP_VIEW_ORIGIN_Y, APP_VIEW_WIDTH, 280)];
    self.secView.backgroundColor = [UIColor whiteColor];
    [self.view addSubview:self.secView];
    
    UILabel *sectitleLable = [[UILabel alloc]initWithFrame:CGRectMake(10, 0,60, 45)];
    sectitleLable.text = @"纠正信息";
    sectitleLable.font = [UIFont systemFontOfSize:14];
    sectitleLable.textColor = APP_TEXTCOLOR;
    sectitleLable.textAlignment = NSTextAlignmentCenter;
    [self.secView addSubview:sectitleLable];
    
    UIView *lineView = [[UIView alloc]initWithFrame:CGRectMake(0, 45, APP_VIEW_WIDTH, 1)];
    lineView.backgroundColor = self.view.backgroundColor;
    [self.secView addSubview:lineView];
    
    self.textView = [[UITextView alloc]initWithFrame:CGRectMake(10, 55, APP_VIEW_WIDTH-20, 130)];
    self.textView.layer.borderColor = [UICOLOR(226, 226, 226, 1) CGColor];
    self.textView.layer.borderWidth = 0.6;
    self.textView.delegate = self;
    [self.secView addSubview:self.textView];
    
    self.fristImageView = [[UIButton alloc]initWithFrame:CGRectMake(10, 195, 70, 70)];
    [self.fristImageView setImage:[UIImage imageNamed:@"icon_addpic_unfocused"] forState:UIControlStateNormal];
    [self.fristImageView addTarget:self action:@selector(clickButton) forControlEvents:UIControlEventTouchUpInside];

    [self.secView addSubview:self.fristImageView];
    
    
    self.submitBtn = [[UIButton alloc]initWithFrame:CGRectMake(15, self.secView.frame.origin.y+self.secView.frame.size.height+20, APP_VIEW_WIDTH-30, 40)];
    self.submitBtn.backgroundColor = APP_TEXTCOLOR;
    [self.submitBtn setTitle:@"确定" forState:UIControlStateNormal];
    [self.submitBtn setTitleColor:[UIColor whiteColor] forState:UIControlStateNormal];
    [self.view addSubview:self.submitBtn];
    self.submitBtn.enabled = NO;
    [self.submitBtn addTarget:self action:@selector(clickSubmit) forControlEvents:UIControlEventTouchUpInside];
    
    [self errorInfo];
    
    self.errTableView = [[UITableView alloc]initWithFrame:CGRectMake(buttonV.frame.origin.x,APP_VIEW_ORIGIN_Y+buttonV.frame.origin.y+buttonV.frame.size.height, buttonV.frame.size.width, buttonV.frame.size.height*6)];
    self.errTableView.dataSource = self;
    self.errTableView.delegate = self;
    self.errTableView.backgroundColor = [UIColor clearColor];
    [self.view addSubview:self.errTableView];
    self.errTableView.hidden = YES;
    
    
    self.rectS = [[NSMutableArray alloc]init];
    self.seleImage = [[NSMutableArray alloc]init];
    
    for (int i = 0 ;i<3;i++) {
        NSValue *value = [NSValue valueWithCGRect:CGRectMake(i*10+(i*70)+10, 195,  70,  70)];
        [self.rectS addObject:value];
    }


    
}
-(void)enableNext{
    
    if (self.isSeleContent && self.isSeleInfo) {
        self.submitBtn.backgroundColor = APP_NAVCOLOR;
        self.submitBtn.enabled = YES;
        
    }else{
        self.submitBtn.backgroundColor = APP_TEXTCOLOR;
        self.submitBtn.enabled = NO;
  
    }
    
}
-(CGFloat)tableView:(UITableView *)tableView heightForRowAtIndexPath:(NSIndexPath *)indexPath{
    return 25;
}
-(NSInteger)tableView:(UITableView *)tableView numberOfRowsInSection:(NSInteger)section{
    return self.resultDic.count;
}
-(UITableViewCell *)tableView:(UITableView *)tableView cellForRowAtIndexPath:(NSIndexPath *)indexPath{
    static NSString *identifier = @"errcell";
    UITableViewCell *cell = [tableView dequeueReusableCellWithIdentifier:identifier];
    if (cell == nil) {
        cell = [[UITableViewCell alloc]initWithStyle:UITableViewCellStyleDefault reuseIdentifier:identifier];
    }
    NSDictionary *dic = [self.resultDic objectAtIndex:indexPath.row];
    cell.textLabel.text = [dic objectForKey:@"info"];
    cell.textLabel.font = [UIFont systemFontOfSize:11];
    cell.textLabel.textColor = APP_TEXTCOLOR;
    return cell;
    
    
}
-(void)tableView:(UITableView *)tableView didSelectRowAtIndexPath:(NSIndexPath *)indexPath{
    
    NSDictionary *dic = [self.resultDic objectAtIndex:indexPath.row];
    self.errTextField.text = [dic objectForKey:@"info"];
    self.errTableView.hidden = YES;
    self.isSeleInfo = YES;
    self.errorcode = [NSString stringWithFormat:@"%@",[dic objectForKey:@"value"]];
    [[NSNotificationCenter defaultCenter]postNotificationName:@"enableNext" object:nil];
    
}

-(void)clickErr{
    self.errTableView.hidden = NO;
}

-(void)textViewDidChange:(UITextView *)textView{
    if (textView.text.length>0) {
        self.isSeleContent = YES;
    }else{
        self.isSeleContent = NO;

    }
    [[NSNotificationCenter defaultCenter]postNotificationName:@"enableNext" object:nil];

}


-(void)addPhotoImageS{
    
    if(self.seleImage.count>0){
        self.isSeleContent = YES;

    }else{
        self.isSeleContent = NO;
  
    }
    
    [[NSNotificationCenter defaultCenter]postNotificationName:@"enableNext" object:nil];

    for (UIView *va in self.secView.subviews) {
        if([va isKindOfClass:[BMSQ_imageView class]]){
            [va removeFromSuperview];
        }
    }
    
    for(int i=0;i<self.seleImage.count;i++){
        
        BMSQ_imageView *imageV = [[BMSQ_imageView alloc]initWithFrame:[[self.rectS objectAtIndex:i]CGRectValue]];
        [imageV.BgimageView setImage:[self.seleImage objectAtIndex:i]];
        imageV.delebutton.tag = 100+i;
        imageV.delegate = self;
        [self.secView addSubview:imageV];
        
    }
    
    if (self.seleImage.count<3) {
        self.fristImageView.hidden = NO;
        self.fristImageView.frame = [[self.rectS objectAtIndex:self.seleImage.count]CGRectValue];
        
    }else{
        self.fristImageView.hidden = YES;
    }
    
    
}

-(void)errorInfo{
    [self initJsonPrcClient:@"2"];
    NSMutableDictionary* paramsDic = [[NSMutableDictionary alloc]init];
    __weak typeof(self) weakSelf = self;
    [self.jsonPrcClient invokeMethod:@"errorInfo" withParameters:paramsDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
        weakSelf.resultDic = responseObject;
        [weakSelf.errTableView reloadData];
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
    }];
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
        
        picker.maximumNumberOfSelection = 3 - self.seleImage.count;
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

-(void)clickSubmit{
    
    if (self.errorcode.length == 0) {
        return;
    }
    
    
    if (self.seleImage.count>0) {
        [self sendImage];
    }else{
        
        [self initJsonPrcClient:@"2"];
        NSMutableDictionary* paramsDic = [[NSMutableDictionary alloc]init];
        [SVProgressHUD showWithStatus:@""];
        
        [paramsDic setObject:[gloabFunction getUserCode] forKey:@"userCode"];
        
        NSUserDefaults *userDefults = [NSUserDefaults standardUserDefaults];
        NSString *longitude = [userDefults objectForKey:LONGITUDE];
        NSString *latitude  = [userDefults objectForKey:LATITUDE];
        NSString *cityName = [userDefults objectForKey:SELECITY];
        [paramsDic setObject:longitude forKey:@"longitude"];
        [paramsDic setObject:latitude forKey:@"latitude"];
        [paramsDic setObject:cityName forKey:@"city"];
        
        [paramsDic setObject:[gloabFunction getUserCode] forKey:@"userCode"];
        [paramsDic setObject:self.errorcode forKey:@"errorInfo"];
        [paramsDic setObject:@"" forKey:@"errorImg"];
        [paramsDic setObject:self.shopCode forKey:@"toShopCode"];
        [paramsDic setObject:self.textView.text forKey:@"message"];
        
        NSString* vcode = [gloabFunction getSign:@"addErrorInformation" strParams:[NSString stringWithFormat:@"%@",[gloabFunction getUserCode]]];
        [paramsDic setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
        [paramsDic setObject:vcode forKey:@"vcode"];
        [paramsDic setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
        
        __weak typeof(self) weakSelf = self;
        [self.jsonPrcClient invokeMethod:@"addErrorInformation" withParameters:paramsDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
            [SVProgressHUD showSuccessWithStatus:@"提交成功"];
            [weakSelf.navigationController popViewControllerAnimated:YES];
            
        } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
            [SVProgressHUD showErrorWithStatus:@"加载失败"];
        }];

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
            [paramsDic setObject:[gloabFunction getUserCode] forKey:@"userCode"];
            [paramsDic setObject:self.errorcode forKey:@"errorInfo"];
            [paramsDic setObject:self.imageUrlS forKey:@"errorImg"];
            [paramsDic setObject:self.shopCode forKey:@"toShopCode"];
            [paramsDic setObject:self.textView.text forKey:@"message"];
            
            NSString* vcode = [gloabFunction getSign:@"addErrorInformation" strParams:[NSString stringWithFormat:@"%@",[gloabFunction getUserCode]]];
            [paramsDic setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
            [paramsDic setObject:vcode forKey:@"vcode"];
            [paramsDic setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
            
            __weak typeof(self) weakSelf = self;
            [self.jsonPrcClient invokeMethod:@"addErrorInformation" withParameters:paramsDic success:^(AFHTTPRequestOperation *operation, id responseObject) {
                [SVProgressHUD showSuccessWithStatus:@"提交成功"];
                [weakSelf.navigationController popViewControllerAnimated:YES];
                
            } failure:^(AFHTTPRequestOperation *operation, NSError *error) {
                [SVProgressHUD showErrorWithStatus:@"加载失败"];
            }];
            
            
        }
        
    }];
}

@end
