//
//  BMSQ_SubAlbumPhotoViewController.m
//  BMSQC
//
//  Created by liuqin on 15/9/11.
//  Copyright (c) 2015年 djx. All rights reserved.
//

#import "BMSQ_SubAlbumPhotoViewController.h"
#import "BMSQ_shopCollectionViewCell.h"
#import "SVProgressHUD.h"

#import "PhotoScrollView.h"


@interface BMSQ_SubAlbumPhotoViewController ()<UICollectionViewDataSource,UICollectionViewDelegate,UICollectionViewDelegateFlowLayout>


@property (nonatomic, strong)NSMutableArray *subPhotoArray;

@property(nonatomic, strong)UICollectionView *collectionView;


@end

@implementation BMSQ_SubAlbumPhotoViewController

- (void)viewDidLoad {
    [super viewDidLoad];

  
    [self setNavigationBar];
    [self setNavBackItem];
    [self setNavTitle:self.myTitle];
    self.subPhotoArray = [[NSMutableArray alloc]init];
    
    
    UICollectionViewFlowLayout *flowLayout = [[UICollectionViewFlowLayout alloc]init];
    [flowLayout setScrollDirection:UICollectionViewScrollDirectionVertical];
    self.collectionView = [[UICollectionView alloc]initWithFrame:CGRectMake(0,APP_VIEW_ORIGIN_Y+10, APP_VIEW_WIDTH, APP_VIEW_CAN_USE_HEIGHT) collectionViewLayout:flowLayout];
    self.collectionView.backgroundColor = [UIColor clearColor];
    self.collectionView.delegate = self;
    self.collectionView.dataSource = self;
    [self.view addSubview:self.collectionView];
    
    [self.collectionView registerClass:[BMSQ_shopCollectionViewCell class] forCellWithReuseIdentifier:@"BMSQ_shopCollectionViewCell"];
    
    [self loadData];

}


-(NSInteger)collectionView:(UICollectionView *)collectionView numberOfItemsInSection:(NSInteger)section{
    
    return self.subPhotoArray.count;
    
    
}


-(UICollectionViewCell *)collectionView:(UICollectionView *)collectionView cellForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    static NSString *identifer = @"BMSQ_shopCollectionViewCell" ;
    BMSQ_shopCollectionViewCell *cell = [collectionView dequeueReusableCellWithReuseIdentifier:identifer forIndexPath:indexPath];
    
    if(cell == nil){
        cell = [[BMSQ_shopCollectionViewCell alloc]init];
    }
    
    [cell setSubAlbm:[self.subPhotoArray objectAtIndex:indexPath.row] row:(int)indexPath.row];
    return cell;
    
    
}

-(CGSize)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout sizeForItemAtIndexPath:(NSIndexPath *)indexPath{
    
    return CGSizeMake(APP_VIEW_WIDTH/2, APP_VIEW_WIDTH/2);
}
//
-(UIEdgeInsets)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout insetForSectionAtIndex:(NSInteger)section{
    
    return UIEdgeInsetsMake(0,0,0, 0);
    
}
- (CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout*)collectionViewLayout minimumInteritemSpacingForSectionAtIndex:(NSInteger)section{
    return 0;
}
-(CGFloat)collectionView:(UICollectionView *)collectionView layout:(UICollectionViewLayout *)collectionViewLayout minimumLineSpacingForSectionAtIndex:(NSInteger)section{
    return 0;
}


-(BOOL)collectionView:(UICollectionView *)collectionView shouldSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    return  YES;
}




-(void)collectionView:(UICollectionView *)collectionView didSelectItemAtIndexPath:(NSIndexPath *)indexPath{
    
    
    PhotoScrollView *sc = [[PhotoScrollView alloc]init];
    UIApplication *app = [UIApplication sharedApplication];
    UIWindow *window = app.keyWindow;
    __block PhotoScrollView *_weakPhoto=sc;
    sc.removeSC = ^{
        [_weakPhoto removeFromSuperview];
        
    };
    sc.count = (int)self.subPhotoArray.count;
    [sc setImageDicView:self.subPhotoArray];
    [sc setImage:(int)indexPath.row];
    [window addSubview:sc];
    
    
}























-(void)loadData{
    
    
    
    [SVProgressHUD showWithStatus:@"正在加载"];
    [self initJsonPrcClient:@"2"];
    
    NSMutableDictionary* params = [[NSMutableDictionary alloc]init];
    [params setObject:[NSString stringWithFormat:@"%@",[self.proDic objectForKey:@"code"]] forKey:@"code"];
    
    
    NSString* vcode = [gloabFunction getSign:@"cGetSubAlbumPhoto" strParams:[NSString stringWithFormat:@"%@",[self.proDic objectForKey:@"code"]]];
    [params setObject:[gloabFunction getUserToken] forKey:@"tokenCode"];
    [params setObject:vcode forKey:@"vcode"];
    [params setObject:[gloabFunction getTimestamp] forKey:@"reqtime"];
    __weak typeof(self) wself = self;
    [self.jsonPrcClient invokeMethod:@"cGetSubAlbumPhoto" withParameters:params success:^(AFHTTPRequestOperation *operation, id responseObject) {
        
        
        NSArray *arr = responseObject;
        
        if (arr.count > 0) {
            
            wself.subPhotoArray = responseObject;
            [wself.collectionView reloadData];
//            [SVProgressHUD showSuccessWithStatus:@"加载成功"];
            [SVProgressHUD dismiss];
            
        }else{
            
            [SVProgressHUD showErrorWithStatus:@"加载失败"];

        }
        
      
        
        
    } failure:^(AFHTTPRequestOperation *operation, NSError *error) {\
        [SVProgressHUD showErrorWithStatus:@"加载失败"];
        NSLog(@"请求错误");
        
        
    }];
    

    
}

@end
